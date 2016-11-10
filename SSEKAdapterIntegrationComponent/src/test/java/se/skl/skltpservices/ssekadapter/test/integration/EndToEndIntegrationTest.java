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
package se.skl.skltpservices.ssekadapter.test.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.construct.Flow;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;
import org.w3.wsaddressing10.AttributedURIType;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import se.inera.ifv.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

/**
 * Created by Peter on 2014-08-14.
 */
public class EndToEndIntegrationTest extends AbstractIntegrationTestCase {

	// TODO: Collect Endpoints from configuration
    private static final String REGISTERMEDICALCERTIFICATE_ENDPOINT      = "http://localhost:33001/ssekadapter/registermedicalcertificate/v3";
	
	private static final String LOGICAL_ADDRESS_VS_1 = "VS-1";
    private static final String LOGICAL_ADDRESS_VS_2 = "VS-2";
	private static final String INVALID_LOGICAL_ADDRESS = "XX000000-00";
	
	
	private final RegisterMedicalCertificateResponderInterface registerMedicalCertificateServicesInterface;

    
    JaxbUtil jaxbUtil = new JaxbUtil(RegisterMedicalCertificateResponseType.class);

    
    private se.inera.ifv.registermedicalcertificateresponder.v3.ObjectFactory objectFactory
    = new se.inera.ifv.registermedicalcertificateresponder.v3.ObjectFactory();

    //
    static Object create(JaxWsProxyFactoryBean jaxWs) {
        final HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setConnectionTimeout(0);
        policy.setReceiveTimeout(180000);
        policy.setAllowChunking(true);

        final Object service = jaxWs.create();
        final Client client = ClientProxy.getClient(service);
        ((HTTPConduit) client.getConduit()).setClient(policy);

        // Creating HTTP headers
        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("x-vp-sender-id", Arrays.asList("TheSender"));
        headers.put("x-rivta-original-serviceconsumer-hsaid", Arrays.asList("TheOriginalSender"));

        // Add HTTP headers to the web service request
        client.getRequestContext().put(Message.PROTOCOL_HEADERS, headers);
         
      
        return service;
    }

    public EndToEndIntegrationTest() {
    	setDisposeContextPerClass(true);

    	final JaxWsProxyFactoryBean jaxWs = new JaxWsProxyFactoryBean();

		jaxWs.setServiceClass(RegisterMedicalCertificateResponderInterface.class);
		jaxWs.setAddress(REGISTERMEDICALCERTIFICATE_ENDPOINT);
		registerMedicalCertificateServicesInterface = (RegisterMedicalCertificateResponderInterface) create(jaxWs);
		
    }		
    
    // RegisterMedicalCertificate
    
    @Test
    public void RegisterMedicalCertificateSuccessTest() {
    	AttributedURIType u = new AttributedURIType();
    	u.setValue(LOGICAL_ADDRESS_VS_2);
    	RegisterMedicalCertificateResponseType resp = registerMedicalCertificateServicesInterface.
				registerMedicalCertificate(u, IntegrationTestDataUtil.createRegisterMedicalCertificateType(IntegrationTestDataUtil.NO_TRIGGER));
		assertFalse(resp.getResult()== null);
    }

    @Test
    public void RegisterMedicalCertificateNotFoundTest() {
    	AttributedURIType u = new AttributedURIType();
    	u.setValue(LOGICAL_ADDRESS_VS_2);
        RegisterMedicalCertificateType req = IntegrationTestDataUtil.createRegisterMedicalCertificateType(IntegrationTestDataUtil.NO_TRIGGER);
        RegisterMedicalCertificateResponseType resp = registerMedicalCertificateServicesInterface.registerMedicalCertificate(u, req);
        assertTrue(resp.getResult() != null);
    }

    @Test
    public void RegisterMedicalCertificateSourceSuccessTest() {
    	AttributedURIType u = new AttributedURIType();
    	u.setValue(LOGICAL_ADDRESS_VS_2);
        RegisterMedicalCertificateResponseType response = registerMedicalCertificateServicesInterface.registerMedicalCertificate(u, IntegrationTestDataUtil.createRegisterMedicalCertificateType(IntegrationTestDataUtil.NO_TRIGGER));
        assertFalse(response.getResult() == null);
        
        validateXmlAgainstSchema(objectFactory.createRegisterMedicalCertificateResponse(response),
        		"/schemas/core_components/ISO_dt_subset_1.0.xsd",
        		"/schemas/core_components/ws-addressing-1.0.xsd",
        		"/schemas/core_components/insuranceprocess_certificate_1.0.xsd", 
                "/schemas/core_components/Insuranceprocess_healthreporting_2.0.xsd",               
                "/schemas/core_components/MU7263-RIV_3.1.xsd",
                "/schemas/core_components/MedicalCertificateQuestionsAnswers_1.0.xsd",
                "/schemas/interactions/RegisterMedicalCertificateInteraction/RegisterMedicalCertificateResponder_3.1.xsd");
        
    }
   

    // ---
    
    private void validateXmlAgainstSchema(Object element, String ... xsds) {
        
        String xml = jaxbUtil.marshal(element);
        logger.debug(xml);
        List<Source> schemaFiles = new ArrayList<Source>();
        for (String xsd : xsds) {
            schemaFiles.add(new StreamSource(getClass().getResourceAsStream(xsd)));
        }
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = factory.newSchema(schemaFiles.toArray(new StreamSource[schemaFiles.size()]));
            Validator validator = schema.newValidator();
            validator.setErrorHandler(new ErrorHandler() {
				@Override
				public void warning(SAXParseException exception)
						throws SAXException {
					fail(String.format("Validation warning: %s", exception.getMessage()));
				}
				@Override
				public void error(SAXParseException exception)
						throws SAXException {
					fail(String.format("Validation error: %s", exception.getMessage()));
					
				}
				@Override
				public void fatalError(SAXParseException exception)
						throws SAXException {
					fail(String.format("Validation fatal error: %s", exception.getMessage()));
					
				}
            });
            validator.validate(new StreamSource(new StringReader(xml)));
            assertTrue(true);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }
       
    // ---
    
    @Test
    public void UpdateTakCacheTest() throws Exception {
    	
    	Flow flow = (Flow) getFlowConstruct("update-tak-cache-http-flow");
    	MuleEvent event = getTestEvent("", flow);
    	flow.process(event);
    }
}
