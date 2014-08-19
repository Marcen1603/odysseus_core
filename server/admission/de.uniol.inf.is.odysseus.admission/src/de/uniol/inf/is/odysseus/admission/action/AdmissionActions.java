package de.uniol.inf.is.odysseus.admission.action;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.admission.IAdmissionActionComponent;
import de.uniol.inf.is.odysseus.admission.IAdmissionActions;
import de.uniol.inf.is.odysseus.admission.IAdmissionControl;
import de.uniol.inf.is.odysseus.admission.IAdmissionEvent;
import de.uniol.inf.is.odysseus.admission.activator.AdmissionPlugIn;

public class AdmissionActions implements IAdmissionActions {

	private static IAdmissionControl admissionControl;

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
	public <T extends IAdmissionActionComponent> T getAdmissionActionComponent(Class<T> componentClass) {
		return AdmissionPlugIn.getAdmissionActionComponentRegistry().getAdmissionActionComponent(componentClass);
	}

	@Override
	public void addEvent(IAdmissionEvent event) {
		Preconditions.checkNotNull(event, "Admission event to add must not be null!");
		
		admissionControl.processEventAsync(event);
	}

}
