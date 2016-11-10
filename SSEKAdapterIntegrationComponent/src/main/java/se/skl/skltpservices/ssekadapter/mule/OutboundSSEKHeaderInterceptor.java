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
package se.skl.skltpservices.ssekadapter.mule;

import static se.skl.skltpservices.ssekadapter.mule.OutboundPreProcessor.ROUTE_LOGICAL_ADDRESS;
import static se.skl.skltpservices.ssekadapter.mule.OutboundRouter.X_RIVTA_ORIGINAL_SERVICECONSUMER_HSAID;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.mule.api.MuleEvent;
import org.mule.api.config.MuleProperties;
import org.mule.api.transport.PropertyScope;
import org.mule.module.cxf.CxfConstants;
import org.mule.module.cxf.MuleSoapHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class OutboundSSEKHeaderInterceptor extends AbstractSoapInterceptor {
	
	private static final Logger log = LoggerFactory.getLogger(OutboundSSEKHeaderInterceptor.class);

	private final static String SSEK_NS_URI = "http://schemas.ssek.org/ssek/2006-05-10/";
	private final static String SOAP_NS_URI = "http://schemas.xmlsoap.org/wsdl/soap/";
    private final static String SSEK_HEADER = "SSEK";
    private final static String QUALIFIED_SSEK_HEADER = "ssek:SSEK";
    private final static String SSEK_SENDERID_PROPERTY = "SenderId";
    private final static String SSEK_RECEIVERID_PROPERTY = "ReceiverId";
    private final static String SSEK_TXID_PROPERTY = "TxId";
    private final static String SSEK_TYPE = "ssek:TYPE";
    private final static String SSEK_ORGNR = "ORGNR";
    private final static String SOAP_MUSTUNDERSTAND = "soap:mustUnderstand";
    
	public OutboundSSEKHeaderInterceptor() {
        super(Phase.PRE_LOGICAL);
	}	

	private String senderId;
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	
	@Override
	public void handleMessage(SoapMessage message) throws Fault {

        MuleEvent event = (MuleEvent) message.getExchange().get(CxfConstants.MULE_EVENT);

        if (event == null)
        {
    		log.info("OutboundSSEKHeaderInterceptor event == null");
            return;
        }

        MuleSoapHeaders muleHeaders = new MuleSoapHeaders(event);

        Document owner_doc = DOMUtils.createDocument();

        Element ssek_header = owner_doc.createElementNS(SSEK_NS_URI, QUALIFIED_SSEK_HEADER);
        // setup ssek: namespace prefix declaration so that we can use it.
        ssek_header.setAttribute("xmlns:ssek", SSEK_NS_URI);
        ssek_header.setAttribute("xmlns:soap", SOAP_NS_URI);
        ssek_header.setAttribute(SOAP_MUSTUNDERSTAND, "1");

    	// Sender
		log.debug("senderId="+ senderId);
        Element sender = (Element) ssek_header.appendChild(buildMuleHeader(owner_doc, SSEK_SENDERID_PROPERTY,
            	senderId));
        sender.setAttribute(SSEK_TYPE, SSEK_ORGNR );
        
        // Receiver
        String receiverId=(String)event.getMessage().getInvocationProperty(ROUTE_LOGICAL_ADDRESS);
		log.debug("receiverId=" + receiverId);
        Element receiver = (Element) ssek_header.appendChild(buildMuleHeader(owner_doc, SSEK_RECEIVERID_PROPERTY,
        	receiverId));
        receiver.setAttribute(SSEK_TYPE, SSEK_ORGNR );
        
        // txId
        String corId = (String) event.getMessage().getProperty(UseOrCreateCorrelationIdTransformer.CORRELATION_ID, PropertyScope.SESSION, "" );
		log.debug("txId="+ corId);
        ssek_header.appendChild(buildMuleHeader(owner_doc, SSEK_TXID_PROPERTY,
                corId));


        SoapHeader sh = new SoapHeader(new QName(SSEK_NS_URI, SSEK_HEADER), ssek_header);
        message.getHeaders().add(sh);
		log.info("OutboundSSEKHeaderInterceptor done");
	}

    Element buildMuleHeader(Document owner_doc, String localName, String value)
    {
        Element out = owner_doc.createElementNS(SSEK_NS_URI, "ssek:" + localName);
        if (value != null)
        {
            Text text = owner_doc.createTextNode(value);
            out.appendChild(text);
        }
        return out;
    }
}
