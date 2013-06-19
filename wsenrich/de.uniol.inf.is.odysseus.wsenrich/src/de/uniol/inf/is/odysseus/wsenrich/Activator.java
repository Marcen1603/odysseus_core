package de.uniol.inf.is.odysseus.wsenrich;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.wsenrich.util.ConnectionForWebservicesRegistry;
import de.uniol.inf.is.odysseus.wsenrich.util.HttpGetConnection;
import de.uniol.inf.is.odysseus.wsenrich.util.HttpPostConnection;
import de.uniol.inf.is.odysseus.wsenrich.util.JsonKeyFinder;
import de.uniol.inf.is.odysseus.wsenrich.util.KeyFinderRegistry;
import de.uniol.inf.is.odysseus.wsenrich.util.RequestBuilderRegistry;
import de.uniol.inf.is.odysseus.wsenrich.util.UriGetBuilder;
import de.uniol.inf.is.odysseus.wsenrich.util.UriPostBuilder;
import de.uniol.inf.is.odysseus.wsenrich.util.XmlKeyFinder;

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
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		ConnectionForWebservicesRegistry.register(new HttpGetConnection());
		ConnectionForWebservicesRegistry.register(new HttpPostConnection());
		
		KeyFinderRegistry.register(new XmlKeyFinder());
		KeyFinderRegistry.register(new JsonKeyFinder());
		
		RequestBuilderRegistry.register(new UriGetBuilder());
		RequestBuilderRegistry.register(new UriPostBuilder());
	
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
}

