package de.uniol.inf.is.odysseus.planmanagement.optimization.querysharingoptimizer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	
	@Override
	public void start(BundleContext context) throws Exception {
		//Dictionary<String, String> properties = new Hashtable<String,String>();
		//context.registerService(IQuerySharingOptimizer.class.getName(), new StandardQuerySharingOptimizer(),properties);
		//System.out.println("Query-Sharing-Bundle started");
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}
}
