package se.skl.skltpservices.ssekadapter.mule;


import javax.xml.bind.annotation.*;
 
@XmlRootElement(namespace="http://schemas.ssek.org/ssek/2006-05-10/")

public class SSEK {

    @XmlElement(namespace="http://schemas.ssek.org/ssek/2006-05-10/", name="SenderId")
    private SenderId senderId;
    @XmlElement(namespace="http://schemas.ssek.org/ssek/2006-05-10/", name="ReceiverId")
    private ReceiverId receiverId;
    private String txId;
    
	public SenderId getSenderId() {
		if(senderId==null)
			senderId = new SenderId();
		return senderId;
	}

	public ReceiverId getReceiverId() {
		if(receiverId==null)
			receiverId = new ReceiverId();
		return receiverId;
	}

	public String getTxId() {
		return txId;
	}
	
    @XmlElement(namespace="http://schemas.ssek.org/ssek/2006-05-10/", name="TxId")
	public void setTxId(String txId) {
		this.txId = txId;
	}
	
	static class ReceiverId {
		
	    @XmlValue
	    public String value;

	    @XmlAttribute(name="Type")
	    public String  type;		
	}

	static class SenderId {
		
	    @XmlValue
	    public String value;

	    @XmlAttribute(name="Type")
	    public String  type;		
	}
}
