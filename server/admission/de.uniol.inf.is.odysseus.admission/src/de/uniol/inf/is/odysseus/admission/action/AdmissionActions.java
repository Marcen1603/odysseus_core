package de.uniol.inf.is.odysseus.admission.action;

import de.uniol.inf.is.odysseus.admission.IAdmissionActionComponent;
import de.uniol.inf.is.odysseus.admission.IAdmissionActions;
import de.uniol.inf.is.odysseus.admission.activator.AdmissionPlugIn;

public class AdmissionActions implements IAdmissionActions {

	@Override
	public <T extends IAdmissionActionComponent> T getAdmissionActionComponent(Class<T> componentClass) {
		return AdmissionPlugIn.getAdmissionActionComponentRegistry().getAdmissionActionComponent(componentClass);
	}

}
