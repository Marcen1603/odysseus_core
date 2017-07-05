package de.uniol.inf.is.odysseus.parser.novel.cql.ui;

import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parser.novel.cql.ui.internal.CqlActivator;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class Activator extends CqlActivator {

	@Override
	public void start(BundleContext arg0) throws Exception {
		super.start(arg0);
		OdysseusRCPPlugIn.waitForExecutor();
		registerDataDictionaryListener();
		registerSessionListener();
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
