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
	
	public SSLServerSocketProvider(int port) {
		this.port = port;
	}
	
	private void createSocket() {
	    TrustManager[] tms= SecurityStore.getInstance().getTrustManagers(); 
		KeyManager[] kms = SecurityStore.getInstance().getKeyManagers();   
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