package de.uniol.inf.is.odysseus.video;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.sensormanagement.server.logging.VideoLoggerProtocolHandler;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private static final Logger LOG = LoggerFactory.getLogger(Activator.class);

	static BundleContext getContext() {
		return context;
	}

	private ServiceRegistration<?> videoLoggerService;

	@Override
	public void start(BundleContext bundleContext) throws Exception 
	{
		Activator.context = bundleContext;
		
		try {
			videoLoggerService =  bundleContext.registerService(IProtocolHandler.class.getName(), new VideoLoggerProtocolHandler(), null);
		} catch (NoClassDefFoundError e) {
			LOG.warn("VideoLoggerProtocolHandler requires sensormanagement.server feature");
		}
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception 
	{
		if (videoLoggerService != null) context.ungetService(videoLoggerService.getReference());
		
		Activator.context = null;		 
	}

}
