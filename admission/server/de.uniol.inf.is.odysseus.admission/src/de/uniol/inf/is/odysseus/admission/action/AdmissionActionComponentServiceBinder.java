package de.uniol.inf.is.odysseus.admission.action;

import de.uniol.inf.is.odysseus.admission.IAdmissionActionComponent;
import de.uniol.inf.is.odysseus.admission.activator.AdmissionPlugIn;

public class AdmissionActionComponentServiceBinder {

	// called by OSGi-DS
	public static void bindAdmissionActionComponent(IAdmissionActionComponent serv) {
		AdmissionPlugIn.getAdmissionActionComponentRegistry().addAdmissionActionComponent(serv);
	}

	// called by OSGi-DS
	public static void unbindAdmissionActionComponent(IAdmissionActionComponent serv) {
		AdmissionPlugIn.getAdmissionActionComponentRegistry().removeAdmissionActionComponent(serv);
	}
	
}
