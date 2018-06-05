package de.uniol.inf.is.odysseus.admission.event;
import de.uniol.inf.is.odysseus.admission.IAdmissionControl;
import de.uniol.inf.is.odysseus.admission.event.generation.ExecutorAdmissionEventGenerator;
import de.uniol.inf.is.odysseus.admission.event.generation.TimingAdmissionEventGenerator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;

public class AdmissionEventPlugIn {

	private static final long TIMING_INTERVAL_MILLIS = 5 * 1000;
	private static final ExecutorAdmissionEventGenerator EXECUTOR_EVENT_GENERATOR = new ExecutorAdmissionEventGenerator();
	private static final TimingAdmissionEventGenerator TIMING_EVENT_GENERATOR = new TimingAdmissionEventGenerator(TIMING_INTERVAL_MILLIS);
	
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
		TIMING_EVENT_GENERATOR.start();
	}

	// called by OSGi-DS
	public static void unbindAdmissionControl(IAdmissionControl serv) {
		if (admissionControl == serv) {
			admissionControl = null;
			TIMING_EVENT_GENERATOR.stopRunning();
		}
	}
	
	public static IServerExecutor getExecutor() {
		return executor;
	}
	
	public static IAdmissionControl getAdmissionControl() {
		return admissionControl;
	}
}
