package de.uniol.inf.is.odysseus.sensormanagement.server;

import java.net.BindException;
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

public class SensorServiceStarter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SensorServiceStarter.class);

	public static void start() {
		boolean ssl = OdysseusConfiguration.getBoolean("WebService.SSL");
		startWebService(ssl);
	}

	private static void startWebService(final boolean ssl) {

		// wait for bundle activation
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				if (ssl) {
					while (SecurityProviderServiceBinding.getSecurityProvider() == null) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
				}
				start(ssl);
			}
		});
		t.setDaemon(true);
		t.start();
	}

	private static void start(boolean useSSL) {
		SensorService implementor = new SensorService();
		boolean sslClientAuthentication = OdysseusConfiguration.getBoolean("Webservice.SSL_Client_Authentication");
		try {
			SSLContext ssl = null;
			if (useSSL) {
				ssl = SSLContext.getInstance("TLS");
			}

			KeyManager[] keyManagers = null;
			TrustManager[] trustManagers = null;
			HttpsConfigurator configurator = null;

			if (useSSL) {
				keyManagers = SecurityProviderServiceBinding
						.getSecurityProvider().getKeyManagers();

				if (sslClientAuthentication) {
					trustManagers = SecurityProviderServiceBinding
							.getSecurityProvider().getTrustManagers();
					configurator = new HttpsConfigurator(ssl) {
						@Override
						public void configure(HttpsParameters params) {
							SSLParameters sslParams = getSSLContext()
									.getDefaultSSLParameters();
							sslParams.setNeedClientAuth(true);
							params.setSSLParameters(sslParams);
						}
					};
				} else {
					configurator = new HttpsConfigurator(ssl);
				}

				ssl.init(keyManagers, trustManagers, new SecureRandom());
			}

			int port = Integer.parseInt(OdysseusConfiguration
					.get("WebService.Port"));
			int maxPort = Integer.parseInt(OdysseusConfiguration
					.get("WebService.MaxPort"));

			String localhost = OdysseusConfiguration.get("WebService.Server");

			String contextStr = OdysseusConfiguration.get("WebService.ExecutorContext") + "/sensors";

			while (port <= maxPort) {
				try {
					if (useSSL) {
						Endpoint endpoint = Endpoint.create(implementor);
						HttpsServer httpsServer = HttpsServer.create(
								new InetSocketAddress(localhost, port), port);
						httpsServer.setHttpsConfigurator(configurator);
						HttpContext context = httpsServer
								.createContext(contextStr);
						httpsServer.start();
						endpoint.publish(context);
					} else {
						String webServiceEndpoint = "http://" + localhost + ":"
								+ port + contextStr;
						Endpoint.publish(webServiceEndpoint, implementor);
					}

					LOGGER.info("Webservice published at http"
							+ (useSSL ? "s" : "") + "://" + localhost + ":"
							+ +port + contextStr);
					break;
				} catch (Exception e) {
					if (e.getCause() instanceof BindException) {
						// Ignore if port is already used
					} else {
						e.printStackTrace();
					}
				}
				port++;
			}
		} catch (Exception e) {
			LOGGER.error("Webservice could not be published", e);
		}
	}

}
