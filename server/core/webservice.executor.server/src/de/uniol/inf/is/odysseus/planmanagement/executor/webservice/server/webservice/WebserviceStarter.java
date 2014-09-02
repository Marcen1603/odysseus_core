package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice;


import java.net.InetSocketAddress;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.xml.ws.Endpoint;

import com.sun.net.httpserver.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.SecurityProviderServiceBinding;

public class WebserviceStarter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebserviceStarter.class);
	
	public static void start() {
		WebserviceServer server = new WebserviceServer();
		long port = Integer.parseInt(OdysseusConfiguration.get("WebService.Port"));
		long maxPort = Integer.parseInt(OdysseusConfiguration.get("WebService.MaxPort"));
		Endpoint endpoint = null;
		String webServiceEndpoint = "";
		Exception ex = null;
		while (port <= maxPort ){
			webServiceEndpoint = OdysseusConfiguration
					.get("WebService.Endpoint1")+":"+port+OdysseusConfiguration
					.get("WebService.Endpoint2");
			try {
				endpoint = Endpoint.publish(webServiceEndpoint, server);
				// if no exception if thrown, service endpoint could be established
				// break while
				break;
			} catch (Exception e) {
				ex = e;
			}
			port++;
		}
		if (endpoint != null && endpoint.isPublished()) {
			LOGGER.info("Webservice published at " + webServiceEndpoint);
		}else{
			LOGGER.error("Webservice could not be published", ex);
		}
	}
	
	
	public static void start(final boolean sslClientAuthentification, final boolean sslServerAuthentification) {
		// wait for bundle activation
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while(SecurityProviderServiceBinding.getSecurityProvider() == null) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
				startSSL(sslClientAuthentification,sslServerAuthentification);
			}			
		});	
		t.setDaemon(true);
		t.start();
	}
	
	private static void startSSL(boolean sslClientAuthentification, boolean sslServerAuthentification) {
		try {
			Endpoint endpoint = Endpoint.create(new WebserviceServer());
			SSLContext ssl = SSLContext.getInstance("TLS");

			KeyManager[] keyManagers = null;
			TrustManager[] trustManagers =null;
			HttpsConfigurator configurator = null;

			if (sslServerAuthentification) {
				keyManagers = SecurityProviderServiceBinding.getSecurityProvider().getKeyManagers();
				configurator = new HttpsConfigurator(ssl);
			}
			
			if (sslClientAuthentification) {
				trustManagers = SecurityProviderServiceBinding.getSecurityProvider().getTrustManagers();
				configurator = new HttpsConfigurator(ssl) {
				     @Override
				     public void configure(HttpsParameters params) {
				         SSLParameters sslParams = getSSLContext().getDefaultSSLParameters();
				         sslParams.setNeedClientAuth(true);
				         params.setSSLParameters(sslParams);
				      }
				 };	
			}
			
			ssl.init(keyManagers, trustManagers, new SecureRandom());			
	
			
			
			int port = Integer.parseInt(OdysseusConfiguration.get("WebService.Port"));
			int maxPort = Integer.parseInt(OdysseusConfiguration.get("WebService.MaxPort"));
			
			while (port <= maxPort ){
				try {
					HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress("localhost", port), port);
					httpsServer.setHttpsConfigurator(configurator);
					HttpContext context = httpsServer.createContext(OdysseusConfiguration.get("WebService.Endpoint2"));
					httpsServer.start();
					endpoint.publish(context);
					LOGGER.info("Webservice published at https://localhost:" + port+OdysseusConfiguration.get("WebService.Endpoint2"));
					break;
				} catch (Exception e) {

				}
				port++;
			}			
		} catch (Exception e) {
			LOGGER.error("Webservice could not be published", e);
		}
	}

}
