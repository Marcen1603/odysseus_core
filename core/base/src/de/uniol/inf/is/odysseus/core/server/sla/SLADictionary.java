package de.uniol.inf.is.odysseus.core.server.sla;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

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
	
	private Map<ISession, String> currentSLA;
	
	private SLADictionary() {
		this.sla = new HashMap<String, SLA>();
		this.currentSLA = new HashMap<ISession, String>();
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
		}
        return false;
	}
	
	
	public void setCurrentSLA(ISession user, String slaName) {
		this.currentSLA.put(user, slaName);
	}

	public String getCurrentSLA(ISession user) {
		return this.currentSLA.get(user);
	}
	
	public SLA getSLA(String slaName) {
		return this.sla.get(slaName);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Registered SLAs:\n");
		for (String s : this.sla.keySet()) {
			sb.append("\t").append(s).append("\n");
		}
		sb.append("Currently set SLAs for users:\n");
		for (ISession user : this.currentSLA.keySet()) {
			sb.append("\t").append(user.getUser().getName()).append(": ").append(
					this.currentSLA.get(user)).append("\n");
		}
		return sb.toString();
	}
}
