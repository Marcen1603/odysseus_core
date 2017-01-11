package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class LoadSheddingAdmissionStatusRegistry {

	private static Map<String, ILoadSheddingAdmissionStatusComponent> components = Maps.newHashMap();
	
	public static void addLoadSheddingAdmissionComponent(ILoadSheddingAdmissionStatusComponent component) {
		Preconditions.checkNotNull(component, "Admission status component must not be null!");
		
		components.put(component.getComponentName(), component);
	}
	
	public static void removeLoadSheddingAdmissionComponent(ILoadSheddingAdmissionStatusComponent component) {
		Preconditions.checkNotNull(component, "Admission status component must not be null!");
		
		components.remove(component.getComponentName());
	}
	
	public static boolean hasLoadSheddingAdmissionComponent(String name) {
		if (components.containsKey(name)) {
			return true;
		}
		return false;
	}
	
	public static ILoadSheddingAdmissionStatusComponent getLoadSheddingAdmissionComponent(String name) {
		return components.get(name);
	}
	
}
