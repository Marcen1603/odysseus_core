package de.uniol.inf.is.odysseus.admission.action;

import de.uniol.inf.is.odysseus.admission.IAdmissionActionComponent;
import de.uniol.inf.is.odysseus.admission.IAdmissionEvent;

public class AdmissionActionComponent implements IAdmissionActionComponent {

	public void processEvent( IAdmissionEvent e ) {
		AdmissionActionPlugIn.getAdmissionControl().processEventAsync(e);
	}
	
	public void processEventDelayed( IAdmissionEvent e, int delayMillis ) {
		AdmissionActionPlugIn.getAdmissionControl().processEventDelayedAsync(e, delayMillis);
	}
}
