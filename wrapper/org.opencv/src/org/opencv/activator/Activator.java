package org.opencv.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Activator implements BundleActivator {
	private static final Logger log = LoggerFactory.getLogger(Activator.class);
	
	@Override
	public void start(BundleContext context) throws Exception {
		try {
			log.debug("Loading native libraries...");
		    System.loadLibrary("cv2");
		    System.loadLibrary("opencv_java246");
		    log.info("Native libraries loaded");
		}
		catch(Exception e) {
			log.error("Could not load native libraries");
			e.printStackTrace();
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
	}
}
