package de.uniol.inf.is.odysseus.admission.action;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.admission.IAdmissionActionComponent;

public class AdmissionActionComponentRegistry {

	private final Map<Class<? extends IAdmissionActionComponent>, IAdmissionActionComponent> actionMap = Maps.newHashMap();
	
	public void addAdmissionActionComponent( IAdmissionActionComponent component ) {
		Preconditions.checkNotNull(component, "Admission action component must not be null!");
		
		actionMap.put(component.getClass(), component);
	}
	
	public void removeAdmissionActionComponent( IAdmissionActionComponent component ) {
		Preconditions.checkNotNull(component, "Admission action component must not be null!");
		
		actionMap.remove(component.getClass());
	}
	
	public Collection<Class<? extends IAdmissionActionComponent>> getAllAdmissionActionComponentClasses() {
		return actionMap.keySet();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IAdmissionActionComponent> T getAdmissionActionComponent( Class<T> componentClass ) {
		Preconditions.checkNotNull(componentClass, "Admission action component class must not be null!");
		
		return (T) actionMap.get(componentClass);
	}
	
	public <T extends IAdmissionActionComponent> boolean existsAdmissionActionComponent( Class<T> componentClass ) {
		Preconditions.checkNotNull(componentClass, "Admission action component class must not be null!");
		
		return actionMap.containsKey(componentClass);
	}
}
