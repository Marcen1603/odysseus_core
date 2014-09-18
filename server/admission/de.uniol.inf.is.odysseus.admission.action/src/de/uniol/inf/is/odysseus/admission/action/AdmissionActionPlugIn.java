package de.uniol.inf.is.odysseus.admission.action;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.admission.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;


public class AdmissionActionPlugIn implements BundleActivator {

	private static IServerExecutor executor;
	private static ISession currentSession;
	private static IAdmissionControl admissionControl;

	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		executor = (IServerExecutor)serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (executor == serv) {
			executor = null;
		}
	}
	
	// called by OSGi-DS
	public static void bindAdmissionControl(IAdmissionControl serv) {
		admissionControl = serv;
	}
	
	// called by OSGi-DS
	public static void unbindAdmissionControl(IAdmissionControl serv) {
		if (admissionControl == serv) {
			admissionControl = null;
		}
	}
	
	public static ISession getActiveSession() {
		if( currentSession == null || !currentSession.isValid()) {
			currentSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
		}
		return currentSession;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

	public static IServerExecutor getServerExecutor() {
		return executor;
	}
	
	public static IAdmissionControl getAdmissionControl() {
		return admissionControl;
	}
}
