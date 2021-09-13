RIV-SSEK Adapter
=================
@deprecated

Utveckling av RIVSSEK-adapter är avslutad.

The adapter adds a SSEK header to RIV messges. It also adds signature to the message.

[Wiki](https://skl-tp.atlassian.net/wiki/)

The RIVSSEKAdaptre runs on Mule 3.3.1.

By using this SKLTP distribution the user and the users organisation agrees to comply with the terms specified by Mules' Common Public Attribution License (CPAL https://www.mulesoft.com/cpal )

Configuration files
--------------------
The following configuration files should be added to the mule conf-folder.

+ RIVSSEKAdapter-config-override.properties

This file overrides the properties in RIVSSEKAdapter-config.properties which contains default properties.

+ RIVSSEKAdapter-sign-security.properties

The file contains the properties for WSS4j and should contain the properties listed below. Values surrounded by [...] are configurable.  
   
`org.apache.ws.security.crypto.provider = org.apache.ws.security.components.crypto.Merlin`  
`org.apache.ws.security.crypto.merlin.keystore.type = jks`  
`org.apache.ws.security.crypto.merlin.keystore.password = [password]`   
`org.apache.ws.security.crypto.merlin.keystore.private.password = [password]`  
`org.apache.ws.security.crypto.merlin.file = [certs/keystore.jks]`  
`SIGNATURE_USER=[auth]`

The keystore should contain a private certificate to sign messages and public certificate of the producer for validation.

+ RIVSSEKStub-config-override.properties

Add this to mock server if RIVSSSEAdapterStub is used. It has the same function as RIVSSEKAdapter-config-override.properties to override properties of the stub application.

Sender and receiver
---------------------
The logical address of the SSEK adapter is Ineras organisational number (5565594230). This is the identity configured in TAK. 
The RIV message uses the SOAP header To for the logical address of the producer. This address is also configured in TAK (for example 1234567890) as an organisational number.

The SenderId and ReceiverId of the SSEK header may differ from the logical adresses used in RIV. This is configured in the override property file as

`RIVSSEK\_SENDER_ID=9999999999`  (Logisk adress)  
`RIVSSEK\_SENDER_ORGNR=169999999999`  
`RIVSSEK\_SENDER_CN=ssek.skltp.se`  
`RIVSSEK\_IDENTITY_TYPE= [ORGNR | CN]`  (default)    
`1234567890\_IDENTITY_TYPE= [ORGNR | CN]`  (override)  
`1234567890_ORGNR=161234567890`  
`1234567890_CN=The company`  

The sender will use the same type of id as the receiver.

TAK
-----
In TAK the logical address of the producer should be in two flavors:

1) 1234567890 for the addressing from consumer->vp->adapter
2) RIVSSEK-1234567890 adapter->producer

Otherwise it is not possible to set up routes for both vp and the adapter.
