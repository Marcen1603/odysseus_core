package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.xml.ws.Endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

public class WebServicePublisher {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebServicePublisher.class);		
	private static HttpServer httpServerInstance = null;
	
	private static HttpServer createHttpServer() throws GeneralSecurityException {		
		HttpsConfigurator configurator = null;
		boolean useSSL = OdysseusConfiguration.instance.getBoolean("WebService.SSL");
		if (useSSL) {
			while (SecurityProviderServiceBinding.getSecurityProvider() == null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}

			SSLContext ssl = SSLContext.getInstance("TLS");								
			TrustManager[] trustManagers = null;
			KeyManager[] keyManagers = SecurityProviderServiceBinding.getSecurityProvider().getKeyManagers();

			boolean sslClientAuthentication = OdysseusConfiguration.instance.getBoolean("Webservice.SSL_Client_Authentication");
			if (sslClientAuthentication) {
				trustManagers = SecurityProviderServiceBinding.getSecurityProvider().getTrustManagers();
				configurator = new HttpsConfigurator(ssl) {
					@Override public void configure(HttpsParameters params) {
						SSLParameters sslParams = getSSLContext().getDefaultSSLParameters();
						sslParams.setNeedClientAuth(true);
						params.setSSLParameters(sslParams);
					}
				};
			} else {
				configurator = new HttpsConfigurator(ssl);
			}

			ssl.init(keyManagers, trustManagers, new SecureRandom());
		}

		int port = Integer.parseInt(OdysseusConfiguration.instance.get("WebService.Port"));
		int maxPort = Integer.parseInt(OdysseusConfiguration.instance.get("WebService.MaxPort"));
		String localhost = OdysseusConfiguration.instance.get("WebService.Server");

		while (port <= maxPort) {
			try {
				HttpServer httpServer;

				if (useSSL) {						
					httpServer = HttpsServer.create(new InetSocketAddress(localhost, port), port);
					((HttpsServer) httpServer).setHttpsConfigurator(configurator);
				} else 
					httpServer = HttpServer.create(new InetSocketAddress(localhost, port), port);

				httpServer.start();

				return httpServer;
			} 
			catch (Exception e) {
				if (e.getCause() instanceof BindException || e instanceof BindException) {
					// Ignore if port is already used
				} else {
					e.printStackTrace();
				}
			}
			port++;
		}

		throw new RuntimeException("No free port for web server found");
	}

	public static void publish(Object implementor, String subUrl) {
		LOGGER.info("Trying to start web service");
		try	{
			if (httpServerInstance == null)
				httpServerInstance = createHttpServer();			

			String contextStr = OdysseusConfiguration.instance.get("WebService.ExecutorContext");
			if (subUrl != null && subUrl != "")
				contextStr += "/" + subUrl;
			
			Endpoint endpoint = Endpoint.create(implementor);
			endpoint.publish(httpServerInstance.createContext(contextStr));
			
			boolean useSSL = httpServerInstance instanceof HttpsServer;
			String hostName = httpServerInstance.getAddress().getHostString() + ":" +
							  httpServerInstance.getAddress().getPort();
			LOGGER.info("Webservice published at http" + (useSSL ? "s" : "") + "://" + hostName + contextStr);																
		} catch (Exception e) {
			LOGGER.error("Webservice could not be published", e);
		}			
	}
}
