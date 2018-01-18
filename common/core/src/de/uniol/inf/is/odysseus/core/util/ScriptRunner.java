package de.uniol.inf.is.odysseus.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class ScriptRunner {

	private static final Logger LOG = LoggerFactory.getLogger(ScriptRunner.class);

	private static Object lock = new Object();
	
	public static boolean runScripts(BundleContext ctx, IExecutor executor, String[] pathes, ISession user) {
		boolean executed = false;

		synchronized (lock) {
			if (ctx != null && executor != null) {

				executed = true;
				Bundle bundle = ctx.getBundle();

				for (String path : pathes) {
					URL url = bundle.getEntry(path);
					try {
						runScript(user, url, executor);
					} catch (IOException e) {
						// Could be ignored
					}
				}
			}

		}
		return executed;
	}
	
	public static void runScript(ISession user, URL fileURL, IExecutor executor) throws IOException {
		LOG.trace("Trying to start " + fileURL);
		InputStream inputStream = null;

		LOG.trace("Running script with URLConnection: {}", fileURL);
		try {
			URLConnection con = fileURL.openConnection();
			inputStream = con.getInputStream();

			readAndRunScript(user, executor, inputStream);
		} catch (Exception e) {

		}
	}

	private static void readAndRunScript(ISession user, IExecutor executor, InputStream inputStream) {
		String query = readFileLines(inputStream);

		if (query.length() > 0) {
			ScriptExecuteThread t = new ScriptExecuteThread(executor, query.toString(), user);
			t.start();
		}
	}

	private static String readFileLines(InputStream inputStream) {
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
