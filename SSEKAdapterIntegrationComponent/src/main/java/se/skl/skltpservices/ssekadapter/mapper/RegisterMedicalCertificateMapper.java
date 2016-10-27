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


import javax.xml.bind.JAXBElement;
import javax.xml.stream.XMLStreamReader;

import org.mule.api.MuleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import se.inera.ifv.registermedicalcertificateresponder.v3.ObjectFactory;
import se.inera.ifv.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.skl.skltpservices.ssekadapter.mapper.error.MapperException;
import se.skl.skltpservices.ssekadapter.util.SpringPropertiesUtil;

public class RegisterMedicalCertificateMapper extends AbstractMapper implements Mapper {

    protected static final Logger log = LoggerFactory.getLogger(RegisterMedicalCertificateMapper.class);
    
    private static final JaxbUtil jaxb = new JaxbUtil(RegisterMedicalCertificateType.class, RegisterMedicalCertificateResponseType.class);
    private static final ObjectFactory objectFactory = new ObjectFactory();

    public RegisterMedicalCertificateMapper() {
        schemaValidationActivated = new Boolean(SpringPropertiesUtil.getProperty("SCHEMAVALIDATION-REGISTERMEDICALCERTIFICATE"));
        log.debug("schema validation is activated? " + schemaValidationActivated);
        
        initialiseValidator("/schemas/core_components/ISO_dt_subset_1.0.xsd",
        		"/schemas/core_components/ws-addressing-1.0.xsd",
        		"/schemas/core_components/insuranceprocess_certificate_1.0.xsd", 
                "/schemas/core_components/insuranceprocess_healthreporting_2.0.xsd",               
                "/schemas/core_components/MU7263-RIV_3.1.xsd",
                "/schemas/core_components/MedicalCertificateQuestionsAnswers_1.0.xsd",
                "/schemas/interactions/RegisterMedicalCertificateInteraction/RegisterMedicalCertificateResponder_3.1.xsd");
    }


    
    @Override
    public MuleMessage mapRequest(final MuleMessage message) throws MapperException {
        try {
            final RegisterMedicalCertificateType request = unmarshal(payloadAsXMLStreamReader(message));
            message.setPayload(request);
            return message;
        } catch (Exception err) {
            throw new MapperException("Error when mapping request", err);
        }
    }


    @Override
    public MuleMessage mapResponse(final MuleMessage message) throws MapperException {
    	try {
    		final RegisterMedicalCertificateResponseType response = unmarshalResponse(payloadAsXMLStreamReader(message));
            message.setPayload(marshal(response));
            return message;
    	} catch (Exception err) {
    		throw new MapperException("Error when mapping response", err);
    	}
    }



    //
    protected RegisterMedicalCertificateType unmarshal(final XMLStreamReader reader) {
        try {
            return  (RegisterMedicalCertificateType) jaxb.unmarshal(reader);
        } finally {
            close(reader);
        }
    }

    protected RegisterMedicalCertificateResponseType unmarshalResponse(final XMLStreamReader reader) {
        try {
            return  (RegisterMedicalCertificateResponseType) jaxb.unmarshal(reader);
        } finally {
            close(reader);
        }
    }


    protected String marshal(final RegisterMedicalCertificateResponseType response) {
        final JAXBElement<RegisterMedicalCertificateResponseType> el = objectFactory.createRegisterMedicalCertificateResponse(response);
        String xml = jaxb.marshal(el);
        validateXmlAgainstSchema(xml, log);
        return xml;
    }

    protected String marshal(final RegisterMedicalCertificateType request) {
        final JAXBElement<RegisterMedicalCertificateType> el = objectFactory.createRegisterMedicalCertificate(request);
        String xml = jaxb.marshal(el);
        validateXmlAgainstSchema(xml, log);
        return xml;
    }



	public RegisterMedicalCertificateResponseType mapResponse(RegisterMedicalCertificateResponseType r,
			MuleMessage mockMessage) {
		return r;
	}


}
