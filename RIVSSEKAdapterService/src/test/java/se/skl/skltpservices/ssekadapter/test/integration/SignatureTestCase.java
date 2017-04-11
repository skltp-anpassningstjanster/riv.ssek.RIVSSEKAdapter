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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.construct.Flow;
import org.soitoolkit.commons.mule.test.junit4.AbstractTestCase;


public class SignatureTestCase extends AbstractTestCase {

    public SignatureTestCase() {
        super();
    }

    @Override
    protected String getConfigResources() {
        return "soitoolkit-mule-jms-connector-activemq-embedded.xml"
                + ",RIVSSEKAdapter-common.xml"
                + ",teststub-services/signing-teststub-service.xml"
                + ",teststub-services/validating-teststub-service.xml";
    }
    
    @Test
    public void SignatureTest() throws Exception {
    	String req = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
    	  + "<soap:Header>"
    	  + "<ssek:SSEK soap:mustUnderstand=\"1\" xmlns:ssek=\"http://schemas.ssek.org/ssek/2006-05-10/\">"
    	  + "    <ssek:SenderId>Inera</ssek:SenderId>"
    	  + "    <ssek:ReceiverId>Skandia Liv</ssek:ReceiverId>"
    	  + "    <ssek:TxId>12345678-1234-1234-1234-123456789123</ssek:TxId>"
    	  + "  </ssek:SSEK>"
    	  + "</soap:Header>"
    	  + "<soap:Body xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
    	  + "  <ns1:HelloWorldRequest xmlns:ns1=\"http://schemas.ssek.org/helloworld/2011-11-17\">"
    	  + "    <ns1:Message>Test</ns1:Message>"
    	  + "  </ns1:HelloWorldRequest>"
    	  + "</soap:Body>"
    	+ "</soap:Envelope>";
		
    	logger.info("Signing soap message");
    	Flow flow = (Flow) getFlowConstruct("signing-flow");
    	MuleEvent event = getTestEvent(req, flow);
    	MuleEvent x = flow.process(event);
    	String result=x.getMessageAsString();
    	logger.info(result);
    	assertTrue(result.contains("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"));
    	logger.info("Stopped " + flow.getName());

    	
    	logger.info("Validating soap message");
    	Flow flow1 = (Flow) getFlowConstruct("validating-flow");
    	MuleEvent event1 = getTestEvent(result, flow1);
    	MuleEvent y = flow1.process(event1);
    	
    	logger.info(y.getMessageAsString());
    	logger.info("Stopped " + flow1.getName());
    	assertTrue(result.equals(y.getMessageAsString()));
    }    
}
