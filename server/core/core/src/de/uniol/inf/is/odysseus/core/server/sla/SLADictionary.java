/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.sla;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

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
	/**
	 * Map of all slas registered in the system
	 */
	private Map<String, SLA> registeredSLA;
	/**
	 * Mapping users to their sla
	 */
	private Map<IUser, String> userSLA;
	
	private SLADictionary() {
		this.registeredSLA = new HashMap<String, SLA>();
		this.userSLA = new HashMap<IUser, String>();
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
		this.registeredSLA.put(slaName, sla);
	}

	/**
	 * removes a sla from dictionary
	 * 
	 * @param registeredSLA
	 *            the name of the sla that should be removed
	 * @return the removed sla
	 */
	public SLA removeSLA(String slaName) {
		return this.registeredSLA.remove(slaName);
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
		if (this.registeredSLA.get(slaName) != null) {
			return true;
		}
        return false;
	}
	
	
	public void setUserSLA(IUser user, String slaName) {
		this.userSLA.put(user, slaName);
	}

	public String getUserSLA(IUser user) {
		return this.userSLA.get(user);
	}
	
	public SLA getSLA(String slaName) {
		return this.registeredSLA.get(slaName);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Registered SLAs:\n");
		for (String s : this.registeredSLA.keySet()) {
			sb.append("\t").append(s).append("\n");
		}
		sb.append("Currently set SLAs for users:\n");
		for (IUser user : this.userSLA.keySet()) {
			sb.append("\t").append(user.getName()).append(": ").append(
					this.userSLA.get(user)).append("\n");
		}
		return sb.toString();
	}
}
