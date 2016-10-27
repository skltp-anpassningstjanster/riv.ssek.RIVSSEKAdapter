/**
 * Copyright (c) 2014 Inera AB, <http://inera.se/>
 *
 * This file is part of SKLTP.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.skl.skltpservices.ssekadapter.mapper;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Abstracts all mapper implementations.
 *
 * @see se.skl.skltpservices.ssekadapter.mapper.Mapper
 * @see se.skl.skltpservices.ssekadapter.mapper.XMLBeanMapper
 *
 * @author Peter
 */
public abstract class AbstractMapper {

	protected static final Logger log = LoggerFactory.getLogger(AbstractMapper.class);
	
    // main supported information types,
    public static final String INFO_VKO         = "vko";
    public static final String INFO_VOO         = "voo";
    public static final String INFO_DIA         = "dia";
    public static final String INFO_LKM_ORD     = "lkm-ord";
    public static final String INFO_UND_KKM_KLI = "und-kkm-kli";
    public static final String INFO_UND_BDI     = "und-bdi";
    public static final String INFO_UND_KON     = "und-kon";
    public static final String INFO_UPP         = "upp";

    static final String NS_EN_EXTRACT          = "urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificate:3:rivtabp20";
    public static final String NS_RIV_EXTRACT  = "urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificate:3:rivtabp20";
    
    static final String NS_REGISTERMEDICALCERTIFICATE_3 = "urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificate:3:rivtabp20";
    

    protected boolean schemaValidationActivated = false;
    
    private Schema schema; // each implementation instance will have its own schema
    
    // mapper implementation hash map with RIV service contract operation names (from WSDL) as a key
    private static final HashMap<String, Mapper> map = new HashMap<String, Mapper>();
    static {
        // regisytermedicalcertificate
        map.put(mapperKey(NS_EN_EXTRACT, NS_REGISTERMEDICALCERTIFICATE_3), new RegisterMedicalCertificateMapper());
        map.put(mapperKey(NS_RIV_EXTRACT, NS_REGISTERMEDICALCERTIFICATE_3), new RIVRegisterMedicalCertificate());
        
    }


    /**
     * Returns the actual mapper instance by the name of the (inbound SOAP) service operation.
     *
     * @param sourceNS the source service contract namespace.
     * @param  targetNS the target service contract namespace.
     * @return the corresponding mapper.
     * @throws java.lang.IllegalStateException when no mapper matches the name of the operation.
     */
    public static Mapper getInstance(final String sourceNS, final String targetNS) {
        assert (sourceNS != null) && (targetNS != null);
        final String key = mapperKey(sourceNS, targetNS);
        final Mapper mapper = map.get(key);
        log.debug("Lookup mapper for key: \"{}\" -> {}", key, mapper);
        if (mapper == null) {
            throw new IllegalStateException("Unable to lookup mapper for operation: \"" + key + "\"");
        }
        return mapper;
    }

    /**
     * Returns the {@link javax.xml.stream.XMLStreamReader} from the message.
     *
     * @param message the message.
     * @return the payload as the expected reader.
     */
    protected XMLStreamReader payloadAsXMLStreamReader(final MuleMessage message) {
        if (message.getPayload() instanceof Object[]) {
            final Object[] payload = (Object[]) message.getPayload();
            if (payload.length > 1 && payload[1] instanceof XMLStreamReader) {
                return (XMLStreamReader) payload[1];
            }
        } else if (message.getPayload() instanceof  XMLStreamReader) {
            return (XMLStreamReader) message.getPayload();
        }
        throw new IllegalArgumentException("Unexpected type of message payload (an Object[] with XMLStreamReader was expected): " + message.getPayload());
    }


    //
    private static String mapperKey(final String src, final String dst) {
        return src + "-" + dst;
    }



      //
    protected void close(final XMLStreamReader reader) {
        try {
            reader.close();
        } catch (XMLStreamException | NullPointerException e) {
            ;
        }
    }
    
  
    protected void initialiseValidator(String ... xsds) {
        List<Source> schemaFiles = new ArrayList<Source>();
        for (String xsd : xsds) {
            schemaFiles.add(new StreamSource(getClass().getResourceAsStream(xsd)));
        }
        
        // Note - SchemaFactory is not threadsafe
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            // Note - Schema is threadsafe
        	StreamSource[] sources = schemaFiles.toArray(new StreamSource[schemaFiles.size()]);
            schema = factory.newSchema(sources);
        } catch (SAXException s) {
            throw new RuntimeException(new InstantiationException("Failed to instantiate schema: " + s.getMessage()));
        }
        
    }

    
    protected void validateXmlAgainstSchema(String xml, Logger log) {
        if (schemaValidationActivated) {
            if (StringUtils.isBlank(xml)) {
                log.error("Attempted to validate empty string");
            } else {
                try {
                    // Validator is not threadsafe - create new one for each invocation
                    Validator validator = schema.newValidator();
                    validator.validate(new StreamSource(new StringReader(xml)));
                    log.debug("response passed schema validation");
                } catch (SAXException e) {
                    log.error("response failed schema validation: " + e.getMessage());
                    log.debug(xml);
                } catch (IOException e) {
                    throw new RuntimeException("Unexpected exception whilst validating xml against schema", e);
                } catch (Exception e) {
                    log.error("response failed schema validation - unexpected error " + e.getMessage(),e);
                }
            }
        }
    }

    
}
