package se.skl.skltpservices.ssekadapter.mule;


import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.XmlElement;

 
@XmlRootElement(namespace="http://schemas.ssek.org/ssek/2006-05-10/")

public class SSEK {

    private String senderId;
    private String receiverId;
    private String txId;
    
	public String getSenderId() {
		return senderId;
	}
    @XmlElement(namespace="http://schemas.ssek.org/ssek/2006-05-10/", name="SenderId")
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getReceiverId() {
		return receiverId;
	}
    @XmlElement(namespace="http://schemas.ssek.org/ssek/2006-05-10/", name="ReceiverId")
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public String getTxId() {
		return txId;
	}
    @XmlElement(namespace="http://schemas.ssek.org/ssek/2006-05-10/", name="TxId")
	public void setTxId(String txId) {
		this.txId = txId;
	}

}
