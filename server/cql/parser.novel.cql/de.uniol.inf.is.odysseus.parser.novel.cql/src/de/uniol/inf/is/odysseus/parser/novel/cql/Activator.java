package de.uniol.inf.is.odysseus.parser.novel.cql;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator
{

	@Override
	public void start(BundleContext arg0) throws Exception 
	{
		System.out.println("hello!");
		for(Bundle bundle : arg0.getBundles()) {
			System.out.println("--> bundle name= " + bundle.getSymbolicName());
		}
	}

	@Override
	public void stop(BundleContext arg0) throws Exception 
	{
		
	}

}
