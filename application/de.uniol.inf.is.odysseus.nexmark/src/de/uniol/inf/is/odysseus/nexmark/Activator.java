package de.uniol.inf.is.odysseus.nexmark;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.nexmark.simulation.NexmarkServer;

public class Activator implements BundleActivator {
	private static final Logger logger = LoggerFactory.getLogger( BundleActivator.class );
	
	static private String categories;
	private static final String categoriesFile = "/config/categories.txt";

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		logger.debug("NexMark started ");
		// Right now, just a simple set of parameters
		
		String[] args = new String[5];
		args[0] = "-pr"; 
		args[1] = System.getenv("pr");
		args[2] = "-useNIO";
		String uN = System.getenv("useNIO");
		if (uN != null){
			boolean useNIO = Boolean.parseBoolean(uN);
			if (!useNIO) args[2] = "";
		}
		// Read from GeneratorConfigfile?
		args[3] = "-gcf";
		args[4] = System.getenv("gcf");
		if (args[4] == null || args[4] == ""){
			args[3] = "";
		}

		
		logger.debug("NexMark started "+args[0]+" "+args[1]+" "+args[2]+" "+args[3]+" "+args[4]);
		if (args[1] != null){
			NexmarkServer.main(args);
		}
		URL catURL = context.getBundle().getEntry(categoriesFile);
		logger.debug("Read Categories from "+categoriesFile+" --> "+catURL);
		categories = readCategoryFile(catURL);
		logger.debug("done ");
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}

	static public String getCategoryFile(){
		return categories;
	}
	
	static private String readCategoryFile(URL input) {
		BufferedReader br = null;
		String text = "";

		try {

			// Windows
			br = new BufferedReader(new InputStreamReader(input.openStream()));

			String line = null;
			while ((line = br.readLine()) != null) {
				text += line.trim();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return text;

	}
}
