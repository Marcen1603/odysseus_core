package de.uniol.inf.is.odysseus.server.autostart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

public class Autostart implements BundleActivator {

	private static final String AUTOSTARTFILE[] = {
			"/autostart/autostart.qry",
			OdysseusConfiguration.getHomeDir() + "autostart" + File.separator
					+ "autostart.qry" };
	static private BundleContext context;
	static private IExecutor executor;
	static final Logger LOG = LoggerFactory.getLogger(Autostart.class);
	
	private static boolean autostartRun = false;
	
	private static Object lock = new Object();

	private static void runAutostart() {
		synchronized( lock ) {
			if (!autostartRun && context != null && executor != null) {
				autostartRun = true;
				try {
					Bundle bundle = context.getBundle();
					for (String path : AUTOSTARTFILE) {
						LOG.debug("Trying to start " + path);
						URL fileURL = bundle.getEntry(path);
						InputStream inputStream = null;
						// could be inside of bundle
						if (fileURL != null) {
							LOG.debug("Running autostartfile " + fileURL);
							URLConnection con = fileURL.openConnection();
							inputStream = con.getInputStream();
						}
						// or somewhere else
						if (inputStream == null) {
							LOG.debug("Running autostartfile "+path);
							try {
								inputStream = new FileInputStream(path);
							} catch (Exception e) {
								// Ignore
							}
						}
	
						if (inputStream != null) {
							BufferedReader in = new BufferedReader(
									new InputStreamReader(inputStream));
							String inputLine;
							StringBuffer query = new StringBuffer();
	
							while ((inputLine = in.readLine()) != null) {
								query.append(inputLine).append("\n");
							}
							if (query.length() > 0) {
								AutostartExecuteThread t = new AutostartExecuteThread(executor, query.toString());
								t.start();
							}
							in.close();
						} else {
							LOG.debug("No autostartfile found");
						}
					}
				} catch (Exception e) {
					LOG.error("Could not execute autostart", e);
				} 
			}
		}
	}

	public void unbindExecutor(IExecutor exec) {
		if( exec == executor ) {
			executor = null;
		}
	}

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

}
