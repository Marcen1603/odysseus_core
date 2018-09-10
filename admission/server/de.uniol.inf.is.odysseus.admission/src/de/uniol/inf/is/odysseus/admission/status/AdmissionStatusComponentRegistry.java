package de.uniol.inf.is.odysseus.admission.status;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.admission.IAdmissionStatusComponent;

public class AdmissionStatusComponentRegistry {
	
	private final Map<Class<? extends IAdmissionStatusComponent>, IAdmissionStatusComponent> componentMap = Maps.newHashMap();

	public void addAdmissionStatusComponent( IAdmissionStatusComponent component ) {
		Preconditions.checkNotNull(component, "Admission status component must not be null!");
		
		componentMap.put(component.getClass(), component);
	}
	
	public void removeAdmissionStatusComponent( IAdmissionStatusComponent component ) {
		Preconditions.checkNotNull(component, "Admission status component must not be null!");
		
		componentMap.remove(component.getClass());
	}
	
	public Collection<Class<? extends IAdmissionStatusComponent>> getAllAdmissionStatusComponentClasses() {
		return componentMap.keySet();
	}
	
	@SuppressWarnings("unchecked")
	public <E extends IAdmissionStatusComponent> E getAdmissionStatusComponent( Class<E> componentType ) {
		Preconditions.checkNotNull(componentType, "Admission component Type must not be null!");
		
		return (E) componentMap.get(componentType);
	}
	
	public <E extends IAdmissionStatusComponent> boolean existsAdmissionStatusComponent( Class<E> componentType ) {
		Preconditions.checkNotNull(componentType, "Admission status component type must not be null!");
		
		return componentMap.containsKey(componentType);
	}
}
