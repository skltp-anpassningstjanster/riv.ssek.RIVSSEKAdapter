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
import org.mule.module.cxf.CxfConstants;
import org.mule.module.cxf.MuleSoapHeaders;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class OutboundSSEKHeaderInterceptor extends AbstractSoapInterceptor {

	private final static String SSEK_NS_URI = "http://schemas.ssek.org/ssek/2006-05-10/";
    private final static String SSEK_HEADER = "SSEK";
    private final static String QUALIFIED_SSEK_HEADER = "ssek:SSEK";
    private final static String SSEK_SENDERID_PROPERTY = "SenderId";
    private final static String SSEK_RECEIVERID_PROPERTY = "ReceiverId";
    private final static String SSEK_TXID_PROPERTY = "TxId";
    
	public OutboundSSEKHeaderInterceptor() {
		super(Phase.PRE_LOGICAL);
		// TODO Auto-generated constructor stub
	}	
	public OutboundSSEKHeaderInterceptor(String i, String p) {
		super(i, p);
		// TODO Auto-generated constructor stub
	}

	public OutboundSSEKHeaderInterceptor(String phase) {
		super(phase);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleMessage(SoapMessage message) throws Fault {

        MuleEvent event = (MuleEvent) message.getExchange().get(CxfConstants.MULE_EVENT);

        if (event == null)
        {
            return;
        }

        MuleSoapHeaders muleHeaders = new MuleSoapHeaders(event);

        if (muleHeaders.getCorrelationId() == null && muleHeaders.getReplyTo() == null)
        {
            return;
        }

        Document owner_doc = DOMUtils.createDocument();

        Element ssek_heaader = owner_doc.createElementNS(SSEK_NS_URI, QUALIFIED_SSEK_HEADER);
        // setup ssek: namespace prefix declaration so that we can use it.
        ssek_heaader.setAttribute("xmlns:ssek", SSEK_NS_URI);
        ssek_heaader.setAttribute("soap:mustUnderstand", "1");

        if (muleHeaders.getCorrelationId() != null)
        {
            ssek_heaader.appendChild(buildMuleHeader(owner_doc, SSEK_SENDERID_PROPERTY,
                	(String)event.getMessage().getInboundProperty(X_RIVTA_ORIGINAL_SERVICECONSUMER_HSAID, "")));
            ssek_heaader.appendChild(buildMuleHeader(owner_doc, SSEK_RECEIVERID_PROPERTY,
            	(String)event.getMessage().getInvocationProperty(ROUTE_LOGICAL_ADDRESS)));
            ssek_heaader.appendChild(buildMuleHeader(owner_doc, SSEK_TXID_PROPERTY,
                    muleHeaders.getCorrelationId()));
        }

        SoapHeader sh = new SoapHeader(new QName(SSEK_NS_URI, SSEK_HEADER), ssek_heaader);
        message.getHeaders().add(sh);
		
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
