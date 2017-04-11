package se.skl.skltpservices.ssekadapter.util;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.ws.security.WSConstants; 

public class WSSPassThroughInterceptor extends AbstractSoapInterceptor { 

    private static final Set<QName> HEADERS = new HashSet<QName>(); 
    static { 
        HEADERS.add(new QName(WSConstants.WSSE_NS, WSConstants.WSSE_LN)); 
        HEADERS.add(new QName(WSConstants.WSSE11_NS, WSConstants.WSSE_LN)); 
        HEADERS.add(new QName(WSConstants.ENC_NS, WSConstants.ENC_DATA_LN)); 
        HEADERS.add(new QName("http://schemas.ssek.org/ssek/2006-05-10/", "SSEK"));     
        } 

    public WSSPassThroughInterceptor() { 
        super(Phase.PRE_PROTOCOL); 
    } 

    public WSSPassThroughInterceptor(String phase) { 
        super(phase); 
    } 

    @Override 
    public Set<QName> getUnderstoodHeaders() { 
        return HEADERS; 
    } 

    public void handleMessage(SoapMessage soapMessage) { 
        // do nothing 
    } 
    


} 

