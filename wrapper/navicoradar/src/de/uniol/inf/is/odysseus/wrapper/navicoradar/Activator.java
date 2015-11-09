package de.uniol.inf.is.odysseus.wrapper.navicoradar;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.mep.MEP;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception 
	{
		Activator.context = bundleContext;
		MEP.searchBundleForMepFunctions(context.getBundle());
		try 
		{
			System.loadLibrary("NavicoRadarJava");
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception 
	{
		Activator.context = null;
	}

}