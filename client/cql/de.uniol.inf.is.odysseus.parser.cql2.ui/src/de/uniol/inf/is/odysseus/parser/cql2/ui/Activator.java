package de.uniol.inf.is.odysseus.parser.cql2.ui;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parser.cql2.ui.internal.Cql2Activator;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class Activator extends Cql2Activator {

	private static final Logger LOG = LoggerFactory.getLogger(Cql2Activator.class);

	
	@Override
	public void start(BundleContext arg0) throws Exception {
		super.start(arg0);
		new Thread(){
			public void run() {
				LOG.info("Starting Cql2Activator");
				try {
					OdysseusRCPPlugIn.waitForExecutor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				registerDataDictionaryListener();
				registerSessionListener();
				LOG.info("Starting Cql2Activator done");

			};  
		}.start();
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		super.stop(arg0);
		unregisterDataDictionaryListener();
	}

	public static void registerSessionListener() {
		OdysseusRCPPlugIn.getExecutor().addUpdateEventListener(DataDictionaryListener.getInstance(),
				IUpdateEventListener.SESSION, null);
	}

	public static void registerDataDictionaryListener() {
		final ISession session;
		if ((session = OdysseusRCPPlugIn.getActiveSession()) != null) {
			OdysseusRCPPlugIn.getExecutor().addUpdateEventListener(DataDictionaryListener.getInstance(),
					IUpdateEventListener.DATADICTIONARY, session);
		}
	}

	public static void unregisterDataDictionaryListener() {
		ISession session = null;
		if ((session = OdysseusRCPPlugIn.getActiveSession()) != null) {
			OdysseusRCPPlugIn.getExecutor().removeUpdateEventListener(DataDictionaryListener.getInstance(),
					IUpdateEventListener.DATADICTIONARY, session);
			OdysseusRCPPlugIn.getExecutor().removeUpdateEventListener(DataDictionaryListener.getInstance(),
					IUpdateEventListener.SESSION, session);
		}
	}

}
