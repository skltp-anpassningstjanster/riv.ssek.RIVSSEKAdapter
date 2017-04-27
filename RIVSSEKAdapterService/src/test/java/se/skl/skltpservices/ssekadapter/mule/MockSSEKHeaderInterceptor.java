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

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.mule.api.MuleEvent;
import org.mule.api.transport.PropertyScope;
import org.mule.module.cxf.CxfConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.wsaddressing10.AttributedURIType;
import org.apache.cxf.message.Message;

public class MockSSEKHeaderInterceptor extends AbstractPhaseInterceptor<Message> {
	
	private static final Logger log = LoggerFactory.getLogger(OutboundSSEKHeaderInterceptor.class);

	private final static String SSEK_NS_URI = "http://schemas.ssek.org/ssek/2006-05-10/";
    private final static String SSEK_HEADER = "SSEK";
    private final static String TO_NS_URI = "http://www.w3.org/2005/08/addressing";


    protected static final QName CUSTOM_HEADER_SSEK = new QName(SSEK_NS_URI, SSEK_HEADER, "ssek");
    protected static final QName CUSTOM_HEADER_TO = new QName(TO_NS_URI, "To", "add");
    

	public MockSSEKHeaderInterceptor() {
        super(Phase.PRE_PROTOCOL);
	}	

    public MockSSEKHeaderInterceptor(String phase) {
        super(phase);
    }
    

	private String senderId;
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	@Override
	public void handleMessage(Message message) throws Fault {

        if (!(message instanceof SoapMessage)) {
            return;
        }
        
        SoapMessage m = (SoapMessage) message;
        
        MuleEvent event = (MuleEvent) message.getExchange().get(CxfConstants.MULE_EVENT);

        if (event == null)
        {
    		log.info("OutboundSSEKHeaderInterceptor event == null");
            return;
        }

    	// Sender
		log.debug("senderId="+ senderId);
        
        // Receiver
        String receiverId="SSEK-2";
		log.debug("receiverId=" + receiverId);
        
        // txId
        String corId = (String) event.getMessage().getProperty(UseOrCreateCorrelationIdTransformer.CORRELATION_ID, PropertyScope.SESSION, "" );
		log.debug("txId="+ corId);
       
        
        SSEK myheader = new SSEK();
        myheader.getReceiverId().value=receiverId;
        myheader.getReceiverId().type="ORGNR";
        myheader.getSenderId().value = senderId;
        myheader.getSenderId().type="ORGNR";
        myheader.setTxId(corId);
        SoapHeader header;
        try {
            header = new SoapHeader(CUSTOM_HEADER_SSEK, myheader, new JAXBDataBinding(SSEK.class));
            header.setMustUnderstand(true);
            m.getHeaders().add(header);
        } catch (JAXBException e) {
    		log.error("Failed to create SSEK header", e);
        }
        
    	
		log.info("MockSSEKHeaderInterceptor done. Nr of headers= " + m.getHeaders().size());

	}




}
