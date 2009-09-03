package de.uniol.inf.is.odysseus.nexmark;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.nexmark.simulation.NexmarkServer;

public class Activator implements BundleActivator {
	private static final Logger logger = LoggerFactory.getLogger( BundleActivator.class );
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		logger.debug("NexMark started ");
		// Anm. im Moment nur ein einfacher Parametersatz
		
		String[] args = new String[3];
		args[0] = "-pr"; 
		args[1] = System.getenv("pr");
		args[2] = "-useNIO";
		if (args[1] != null){
			NexmarkServer.main(args);
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}

}
