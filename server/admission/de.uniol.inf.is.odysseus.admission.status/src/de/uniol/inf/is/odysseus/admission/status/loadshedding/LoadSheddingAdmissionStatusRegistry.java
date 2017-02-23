package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.Map;

import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class LoadSheddingAdmissionStatusRegistry {
	
	private static int defaultMaxSheddingFactor = 50;
	
	private static int sheddingGrowth = 20;

	private static Map<String, ILoadSheddingAdmissionStatusComponent> components = Maps.newHashMap();
	
	private static QuerySelectionStrategy selectionStrategy = QuerySelectionStrategy.DEFAULT;
	
	private static ILoadSheddingAdmissionStatusComponent activeComponent;
	
	private static boolean active = false;
	
	private static boolean first = true;
	
	public static void addLoadSheddingAdmissionComponent(ILoadSheddingAdmissionStatusComponent component) {
		Preconditions.checkNotNull(component, "Admission status component must not be null!");
		
		components.put(component.getComponentName(), component);
		
		if (component instanceof SimpleLoadSheddingAdmissionStatusComponent) {
			setActiveComponent(component.getComponentName());
		}
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

	public static int getSheddingGrowth() {
		return sheddingGrowth;
	}

	public static void setSheddingGrowth(int sheddingGrowth) {
		if (active) {
			throw new ActiveLoadSheddingException();
		}
		LoadSheddingAdmissionStatusRegistry.sheddingGrowth = sheddingGrowth;
	}

	public static ILoadSheddingAdmissionStatusComponent getActiveComponent() {
		return activeComponent;
	}

	public static void setActiveComponent(String activeComponentName) {
		if (active) {
			throw new ActiveLoadSheddingException();
		}
		LoadSheddingAdmissionStatusRegistry.activeComponent = getLoadSheddingAdmissionComponent(activeComponentName);
	}

	public static int getDefaultMaxSheddingFactor() {
		return defaultMaxSheddingFactor;
	}

	public static void setDefaultMaxSheddingFactor(int defaultMaxSheddingFactor) {
		if (active) {
			throw new ActiveLoadSheddingException();
		}
		LoadSheddingAdmissionStatusRegistry.defaultMaxSheddingFactor = defaultMaxSheddingFactor;
	}

	public static QuerySelectionStrategy getSelectionStrategy() {
		return selectionStrategy;
	}

	public static void setSelectionStrategy(QuerySelectionStrategy selectionStrategy) {
		if (active) {
			LoggerFactory.getLogger(LoadSheddingAdmissionStatusRegistry.class).info("AcitveLoadSheddingException");
			throw new ActiveLoadSheddingException();
		}
		LoadSheddingAdmissionStatusRegistry.selectionStrategy = selectionStrategy;
	}
	
	public static void setActive(boolean active) {
		LoggerFactory.getLogger(LoadSheddingAdmissionStatusRegistry.class).info("setactive");
		LoadSheddingAdmissionStatusRegistry.active = active;
	}

	public static boolean isFirst() {
		return first;
	}

	public static void setFirst(boolean first) {
		LoadSheddingAdmissionStatusRegistry.first = first;
	}
	
}
