package se.skl.skltpservices.ssekadapter.util;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
 
import org.apache.ws.security.WSPasswordCallback;

public class SignKeystorePasswordCallback implements CallbackHandler {
 
    private static final String PASSWORD_PROPERTY_NAME = "org.apache.ws.security.crypto.merlin.keystore.password";  
 
    private static String password;
    static {
        password = SpringPropertiesUtil.getProperty(PASSWORD_PROPERTY_NAME);
    }   
 
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
 
        // set the password for our message.
        pc.setPassword(password);
    }
}

