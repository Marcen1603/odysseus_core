package de.uniol.inf.is.odysseus.peer.ddc.file.plugin;

import java.io.IOException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.ddc.DDC;
import de.uniol.inf.is.odysseus.peer.ddc.file.DDCFileHandler;

/**
 * The activator for the DDC file bundle loads DDC entries from
 * {@link DDCFileHandler#DDC_FILE_NAME} while starting and stores DDC entries in
 * {@link DDCFileHandler#DDC_FILE_NAME} while stopping.
 * 
 * @author Michael Brand
 *
 */
public class Activator implements BundleActivator {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(Activator.class);

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		try {
			DDCFileHandler.load();
		} catch (IOException e) {
			Activator.LOG.error("Could not load DDC file!", e);
		}
		
		// distribute data
		DDC ddc = DDC.getInstance();
		if (ddc != null){ 
			//DDCAdvertisement advertisement = DDCAdvertisementGenerator.generate(ddc);
		}
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {

		try {

			DDCFileHandler.save();

		} catch (IOException e) {

			Activator.LOG.error("Could not save DDC file!", e);

		}

	}

}