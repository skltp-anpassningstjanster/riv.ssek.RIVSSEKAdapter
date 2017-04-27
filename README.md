RIV-SSEK Adapter
=================
The adapter adds a SSEK header to RIV messges. It also adds signature to the message.

[Wiki](https://skl-tp.atlassian.net/wiki/)

The RIVSSEKAdaptre runs on Mule 3.3.1.

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
`org.apache.ws.security.crypto.merlin.file = [certs/kesystore.jks]`  
`SIGNATURE_USER=[auth]`

+ RIVSSEKStub-config-override.properties

Add this to mock server if RIVSSSEAdapterStub is used. It has the same function as RIVSSEKAdapter-config-override.properties to override properties of the stub application.
