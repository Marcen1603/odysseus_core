package de.uniol.inf.is.odysseus.admission.event;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.admission.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;

public class AdmissionEventPlugIn implements BundleActivator {

	private static final ExecutorAdmissionEventGenerator EXECUTOR_EVENT_GENERATOR = new ExecutorAdmissionEventGenerator();
	
	private static IServerExecutor executor;
	private static IAdmissionControl admissionControl;

	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		executor = (IServerExecutor)serv;
		
		executor.addPlanModificationListener(EXECUTOR_EVENT_GENERATOR);
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (executor == serv) {
			executor.removePlanModificationListener(EXECUTOR_EVENT_GENERATOR);
			
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
	
	@Override
	public void start(BundleContext context) throws Exception {
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

	public static IServerExecutor getExecutor() {
		return executor;
	}
	
	public static IAdmissionControl getAdmissionControl() {
		return admissionControl;
	}
}
