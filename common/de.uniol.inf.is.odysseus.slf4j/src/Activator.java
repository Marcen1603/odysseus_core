import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.slf4j.LoggingConfiguration;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		LoggingConfiguration.load();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
	}

}
