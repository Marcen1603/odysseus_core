package de.uniol.inf.is.odysseus.server.autostart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

public class Autostart implements BundleActivator {

	private static final Logger LOG = LoggerFactory.getLogger(Autostart.class);
	private static final String AUTOSTARTFILE[] = { "/autostart/autostart.qry", OdysseusConfiguration.getHomeDir() + "autostart" + File.separator + "autostart.qry" };
	
	private static Object lock = new Object();
	
	private static BundleContext context;
	private static IExecutor executor;
	private static boolean autostartExecuted = false;

	// called by OSGi-DS
	public void unbindExecutor(IExecutor exec) {
		if (exec == executor) {
			executor = null;
		}
	}

	// called by OSGi-DS
	public void bindExecutor(IExecutor exec) {
		executor = exec;
		runAutostart();
	}

	@Override
	public void start(BundleContext ctx) throws Exception {
		context = ctx;
		runAutostart();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

	private static void runAutostart() {
		synchronized (lock) {
			if (!autostartExecuted && context != null && executor != null) {
				autostartExecuted = true;
				
				Bundle bundle = context.getBundle();
				for (String path : AUTOSTARTFILE) {
					LOG.debug("Trying to start " + path);
					URL fileURL = bundle.getEntry(path);
					InputStream inputStream = null;

					if (fileURL != null) {
						LOG.debug("Running autostartfile with URLConnection: {}", fileURL);

						Optional<InputStream> optInputStream = getInputStream(fileURL);
						inputStream = optInputStream.isPresent() ? optInputStream.get() : null;
					}

					if (inputStream == null) {
						LOG.debug("Running autostartfile with FileInputStream: {} ", path);
						try {
							inputStream = new FileInputStream(path);
						} catch (Exception e) {
						}
					}

					if (inputStream != null) {
						String query = readAutostartFileLines(inputStream);
						
						if (query.length() > 0) {
							AutostartExecuteThread t = new AutostartExecuteThread(executor, query.toString());
							t.start();
						}
					} else {
						LOG.debug("Autostartfile {} not found or failed to load", path);
					}
				}
			}
		}
	}

	private static Optional<InputStream> getInputStream(URL fileURL) {
		try {
			URLConnection con = fileURL.openConnection();
			return Optional.of(con.getInputStream());
		} catch (IOException e) {
			LOG.debug("Could not open URLConnection to {}", fileURL);
		}
		return Optional.absent();
	}

	private static String readAutostartFileLines(InputStream inputStream) {
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		String inputLine = null;
		StringBuffer query = new StringBuffer();

		try {
			while ((inputLine = in.readLine()) != null) {
				query.append(inputLine).append("\n");
			}
			
			in.close();
		} catch (IOException ignore) {
		}
		return query.toString();
	}
}
