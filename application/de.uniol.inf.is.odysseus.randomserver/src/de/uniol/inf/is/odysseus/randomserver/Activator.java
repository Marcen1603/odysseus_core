package de.uniol.inf.is.odysseus.randomserver;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

public class Activator implements BundleActivator {

	private BundleContext context;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		this.context = context;
		Dictionary<String, Integer> props = new Hashtable<String, Integer>();		
		props.put(Constants.SERVICE_RANKING, new Integer(Integer.MAX_VALUE-100));
		context.registerService(CommandProvider.class.getName(), RandomServer.getInstance(), props);		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {		
	}

	public BundleContext getContext() {
		return this.context;
	}

}
