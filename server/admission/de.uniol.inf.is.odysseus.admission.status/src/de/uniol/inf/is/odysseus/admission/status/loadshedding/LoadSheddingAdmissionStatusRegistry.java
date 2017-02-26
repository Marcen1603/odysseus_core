package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.admission.status.loadshedding.exceptions.ActiveLoadSheddingException;
import de.uniol.inf.is.odysseus.admission.status.loadshedding.exceptions.NoSuchStatusComponentException;

/**
 * In this registry are all load shedding status components stored.
 * Also provides this class all attributes, which can be changed by the user.
 */
public class LoadSheddingAdmissionStatusRegistry {

	private volatile static int defaultMaxSheddingFactor = 50;
	
	private volatile static int sheddingGrowth = 20;

	/**
	 * All ILoadSheddingAdmissionStatusComponents are stored here.
	 */
	private volatile static Map<String, ILoadSheddingAdmissionStatusComponent> components = new HashMap<>();
	
	private volatile static QuerySelectionStrategy selectionStrategy = QuerySelectionStrategy.DEFAULT;
	
	private volatile static ILoadSheddingAdmissionStatusComponent activeComponent;
	
	private volatile static boolean active = false;
	
	/**
	 * Stores the given ILoadSheddingAdmissionStatusComponent for later use.
	 * @param component ILoadSheddingAdmissionStatusComponent
	 */
	public static void addLoadSheddingAdmissionComponent(ILoadSheddingAdmissionStatusComponent component) {
		Preconditions.checkNotNull(component, "Admission status component must not be null!");
		
		components.put(component.getComponentName(), component);
		
		if (component instanceof SimpleLoadSheddingAdmissionStatusComponent) {
			setActiveComponent(component.getComponentName());
		}
	}
	
	/**
	 * Removes the given ILoadSheddingAdmissionStatusComponent.
	 * @param component ILoadSheddingAdmissionStatusComponent
	 */
	public static void removeLoadSheddingAdmissionComponent(ILoadSheddingAdmissionStatusComponent component) {
		Preconditions.checkNotNull(component, "Admission status component must not be null!");
		
		components.remove(component.getComponentName());
	}
	
	/**
	 * Returns true if a ILoadSheddingAdmissionStatusComponent with the given name was was stored.
	 * @param name String
	 * @return
	 */
	public static boolean hasLoadSheddingAdmissionComponent(String name) {
		if (components.containsKey(name)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns the ILoadSheddingAdmissionStatusComponent with the given name.
	 * 
	 * If no such component was stored a NoSuchStatusComponentException is thrown
	 * @param name String
	 * @return ILoadSheddingAdmissionStatusComponent
	 */
	public static ILoadSheddingAdmissionStatusComponent getLoadSheddingAdmissionComponent(String name) {
		if (components.containsKey(name)) {
			return components.get(name);
		} else {
			throw new NoSuchStatusComponentException();
		}
	}

	public static int getSheddingGrowth() {
		return sheddingGrowth;
	}

	public static void setSheddingGrowth(int sheddingGrowth) {
		if (active) {
			throw new ActiveLoadSheddingException();
		}
		LoadSheddingAdmissionStatusRegistry.sheddingGrowth = sheddingGrowth;
		LoggerFactory.getLogger(LoadSheddingAdmissionStatusRegistry.class).info("Changed the sheddingGrowth to " + sheddingGrowth);
	}

	public static ILoadSheddingAdmissionStatusComponent getActiveComponent() {
		return activeComponent;
	}

	public static void setActiveComponent(String activeComponentName) {
		if (active) {
			throw new ActiveLoadSheddingException();
		}
		LoadSheddingAdmissionStatusRegistry.activeComponent = getLoadSheddingAdmissionComponent(activeComponentName);
		LoggerFactory.getLogger(LoadSheddingAdmissionStatusRegistry.class)
			.info("Changed the active component to " + activeComponentName);
	}

	public static int getDefaultMaxSheddingFactor() {
		return defaultMaxSheddingFactor;
	}

	public static void setDefaultMaxSheddingFactor(int defaultMaxSheddingFactor) {
		if (active) {
			throw new ActiveLoadSheddingException();
		}
		LoadSheddingAdmissionStatusRegistry.defaultMaxSheddingFactor = defaultMaxSheddingFactor;
		LoggerFactory.getLogger(LoadSheddingAdmissionStatusRegistry.class)
			.info("Changed the default maximal shedding factor to " + defaultMaxSheddingFactor);
	}

	public static QuerySelectionStrategy getSelectionStrategy() {
		return selectionStrategy;
	}

	public static void setSelectionStrategy(QuerySelectionStrategy selectionStrategy) {
		if (active) {
			throw new ActiveLoadSheddingException();
		}
		LoadSheddingAdmissionStatusRegistry.selectionStrategy = selectionStrategy;
		LoggerFactory.getLogger(LoadSheddingAdmissionStatusRegistry.class)
			.info("Changed the selection strategy to " + selectionStrategy.toString());
	}
	
	public static void setActive(boolean active) {
		LoadSheddingAdmissionStatusRegistry.active = active;
	}
	
}
