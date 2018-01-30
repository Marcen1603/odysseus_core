package de.uniol.inf.is.odysseus.server.autostart;

import java.io.File;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.ScriptRunner;

public class Autostart implements BundleActivator {

	private static Object lock = new Object();

	private static BundleContext context;
	private static IExecutor executor;
	private static boolean autostartExecuted = false;

	@SuppressWarnings("deprecation")
	private static String[] PATHES = { "/autostart/autostart.qry",
			OdysseusConfiguration.instance.getHomeDir() + "autostart" + File.separator + "autostart.qry" };

	// called by OSGi-DS
	public void unbindExecutor(IExecutor exec) {
		if (exec == executor) {
			executor = null;
		}
	}

	// called by OSGi-DS
	public void bindExecutor(IExecutor exec) {
		executor = exec;
		runAutostartScripts(context, executor);
	}

	@Override
	public void start(BundleContext ctx) throws Exception {
		context = ctx;
		runAutostartScripts(context, executor);
	}

	private void runAutostartScripts(BundleContext context, IExecutor executor) {
		synchronized (lock) {

			if (!autostartExecuted && context != null && executor != null) {

				@SuppressWarnings("deprecation")
				ISession user = SessionManagement.instance.loginSuperUser(null);
				autostartExecuted = ScriptRunner.runScripts(context, executor, PATHES, user);
			}
		}

	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
