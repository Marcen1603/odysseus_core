package de.uniol.inf.is.odysseus.wrapper.baslercamera;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.wrapper.baslercamera.swig.BaslerCamera;

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
			System.loadLibrary("BaslerJava");
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}		
		
		BaslerCamera.initializeSystem();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception 
	{
		BaslerCamera.shutDownSystem();
		Activator.context = null;
	}

}
