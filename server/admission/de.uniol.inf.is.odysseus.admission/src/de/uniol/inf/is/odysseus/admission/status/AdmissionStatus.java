package de.uniol.inf.is.odysseus.admission.status;

import de.uniol.inf.is.odysseus.admission.IAdmissionStatus;
import de.uniol.inf.is.odysseus.admission.IAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.admission.activator.AdmissionPlugIn;

public class AdmissionStatus implements IAdmissionStatus {

	@Override
	public <T extends IAdmissionStatusComponent> T getStatusComponent(Class<T> statusType) {
		return AdmissionPlugIn.getAdmissionStatusComponentRegistry().getAdmissionStatusComponent(statusType);
	}

	@Override
	public boolean hasStatusComponent(Class<? extends IAdmissionStatusComponent> statusType) {
		return AdmissionPlugIn.getAdmissionStatusComponentRegistry().existsAdmissionStatusComponent(statusType);
	}
	
}
