package de.uniol.inf.is.odysseus.security.ssl;

import java.net.ServerSocket;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManager;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.IServerSocketProvider;
import de.uniol.inf.is.odysseus.security.store.SecurityStore;

public class SSLServerSocketProvider implements IServerSocketProvider {
	
	private ServerSocket socket;
	private int port;
	private boolean clientAuthentication;
	
	public SSLServerSocketProvider(int port, boolean clientAuthentication) {
		this.port = port;
		this.clientAuthentication = clientAuthentication;
	}
	
	private void createSocket() {
		KeyManager[] kms = SecurityStore.getInstance().getKeyManagers();   
	    TrustManager[] tms = null;
	    if (clientAuthentication) {
		   tms= SecurityStore.getInstance().getTrustManagers();
	    } 
        try {
            SSLContext context=SSLContext.getInstance("TLS");
            context.init(kms, tms, new java.security.SecureRandom());        
    		SSLServerSocketFactory sslServerSocketfactory = context.getServerSocketFactory();   
            this.socket =  sslServerSocketfactory.createServerSocket(port);
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new RuntimeException("Error while creating ssl socket",e);
        }
	}


	@Override
	public ServerSocket getServerSocket() {
		if (this.socket == null) {
			this.createSocket();
		}
		return this.socket;
	}

}