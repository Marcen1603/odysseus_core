package de.uniol.inf.is.odysseus.admission.status;

import de.uniol.inf.is.odysseus.admission.IAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.admission.activator.AdmissionPlugIn;

public class AdmissionStatusServiceBinder {

	// called by OSGi-DS
	public static void bindAdmissionStatusComponent(IAdmissionStatusComponent serv) {
		AdmissionPlugIn.getAdmissionStatusComponentRegistry().addAdmissionStatusComponent(serv);
	}

	// called by OSGi-DS
	public static void unbindAdmissionStatusComponent(IAdmissionStatusComponent serv) {
		AdmissionPlugIn.getAdmissionStatusComponentRegistry().removeAdmissionStatusComponent(serv);
	}
}
