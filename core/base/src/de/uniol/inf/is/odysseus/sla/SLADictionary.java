package de.uniol.inf.is.odysseus.sla;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton class for saving all created service level agreements and looking
 * them up when used in queries. Further this class provides the global
 * interface for changes of sla related stuff (like adding a query with a 
 * certain sla) and notifying other objects, that are interested in these 
 * changes (e.g. the sla scheduler)  
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SLADictionary {

	private static SLADictionary instance;

	private Map<String, SLA> sla;
	
	private List<ISLAChangedEventListener> listeners;

	private SLADictionary() {
		this.sla = new HashMap<String, SLA>();
		this.listeners = new ArrayList<ISLAChangedEventListener>();
	}

	/**
	 * @return the single instance of the sla dictionary
	 */
	public static synchronized SLADictionary getInstance() {
		if (instance == null) {
			instance = new SLADictionary();
		}
		return instance;
	}

	/**
	 * adds a sla to the dictionary
	 * 
	 * @param slaName
	 *            the name of the sla
	 * @param sla
	 *            the sla to save
	 */
	public void addSLA(String slaName, SLA sla) {
		this.sla.put(slaName, sla);
	}

	/**
	 * removes a sla from dictionary
	 * 
	 * @param sla
	 *            the name of the sla that should be removed
	 * @return the removed sla
	 */
	public SLA removeSLA(String slaName) {
		return this.sla.remove(slaName);
	}

	/**
	 * checks if a sla with the given name already exists in the dictionary
	 * 
	 * @param slaName
	 *            the name to look up
	 * @return true iff a sla with the given name already exists in dictionary,
	 *         false otherwise
	 */
	public boolean exists(String slaName) {
		if (this.sla.get(slaName) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * broadcasts {@link SLAChangedEvent} to all registered listeners
	 * @param event the event to broadcast
	 */
	private void fireEvents(SLAChangedEvent event) {
		for (ISLAChangedEventListener listener : this.listeners) {
			listener.slaChanged(event);
		}
	}
	
	public void addSLAChangedEventListener(ISLAChangedEventListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeSLAChangedEventListener(ISLAChangedEventListener listener) {
		this.listeners.remove(listener);
	}

}
