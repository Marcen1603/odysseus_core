package de.uniol.inf.is.odysseus.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.Optional;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class ScriptRunner {

	private static final Logger LOG = LoggerFactory.getLogger(ScriptRunner.class);

	public static boolean runScripts(BundleContext ctx, IExecutor executor, String[] pathes, ISession user) {
		boolean executed = false;

		if (ctx != null && executor != null) {

			executed = true;
			Bundle bundle = ctx.getBundle();

			for (String path : pathes) {
				URL url = bundle.getEntry(path);

				if (url == null) {
					try {
						if (!path.startsWith("http")) {
							// Distinguish relative and absolute paths
							if (Paths.get(path).isAbsolute()) {
							    url = new URL("file:///"+path);                               
							} else {
							    url = new URL(new URL("file:"), path);                                
							}
						}
					} catch (MalformedURLException e) {
						LOG.error("Error loading script from " + path, e);
					}
				}

				try {
					runScript(user, url, executor);
				} catch (IOException e) {
					// Could be ignored
					LOG.error("Error running script ", e);
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
			Optional<String> path;
			if (fileURL.getProtocol().equalsIgnoreCase("file")) {
				path = Optional.of(getBasePath(fileURL.getPath()));
			}else if (fileURL.getProtocol().startsWith("bundle")) {
				path = Optional.of(getBasePath(fileURL.getFile()));
			}else {
				path = Optional.empty();
			}
			readAndRunScript(user, executor, inputStream, path);
		} catch (Exception e) {

		}
	}

	private static String getBasePath(String fileURL) {
		return fileURL.substring(0, fileURL.lastIndexOf("/")+1);
	}

	private static void readAndRunScript(ISession user, IExecutor executor, InputStream inputStream, Optional<String> fileUrl) {
		String query = readFileLines(inputStream);
		
		if (query.length() > 0) {
			if (fileUrl.isPresent()) {
				query = replaceRootPathInFile(query, fileUrl.get());
			}
			
			ScriptExecuteThread t = new ScriptExecuteThread(executor, query.toString(), user);
			t.start();
		}
	}
	
	private static String replaceRootPathInFile(String queryFileStr, String rootPath) {
		return queryFileStr.replace("${BUNDLE-ROOT}", rootPath);
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
