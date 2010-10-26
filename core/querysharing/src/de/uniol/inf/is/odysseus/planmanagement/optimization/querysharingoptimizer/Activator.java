package de.uniol.inf.is.odysseus.planmanagement.optimization.querysharingoptimizer;

import java.util.Hashtable;
import java.util.Dictionary;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.planmanagement.optimization.querysharing.IQuerySharingOptimizer;

public class Activator implements BundleActivator {
	
	public void start(BundleContext context) throws Exception {
		//Dictionary<String, String> properties = new Hashtable<String,String>();
		//context.registerService(IQuerySharingOptimizer.class.getName(), new StandardQuerySharingOptimizer(),properties);
		//System.out.println("Query-Sharing-Bundle started");
	}

	public void stop(BundleContext context) throws Exception {

	}
}
