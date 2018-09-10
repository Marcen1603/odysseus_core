package de.uniol.inf.is.odysseus.admission.status;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.admission.IAdmissionStatus;
import de.uniol.inf.is.odysseus.admission.IAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.admission.activator.AdmissionPlugIn;

public class AdmissionStatus implements IAdmissionStatus {

	private final Map<Class<? extends IAdmissionStatusComponent>, IAdmissionStatusComponent> cachedComponents = Maps.newHashMap();
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IAdmissionStatusComponent> T getStatusComponent(Class<T> statusType) {
		if( cachedComponents.containsKey(statusType)) {
			return (T) cachedComponents.get(statusType);
		}
		T newComponent = AdmissionPlugIn.getAdmissionStatusComponentRegistry().getAdmissionStatusComponent(statusType);
		cachedComponents.put(statusType, newComponent);
		
		return newComponent;
	}

	@Override
	public boolean hasStatusComponent(Class<? extends IAdmissionStatusComponent> statusType) {
		return AdmissionPlugIn.getAdmissionStatusComponentRegistry().existsAdmissionStatusComponent(statusType);
	}
	
}
