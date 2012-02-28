/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.util;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.eclipse.core.runtime.preferences.ConfigurationScope;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class ConnectPreferencesManager {
	
	private static ConnectPreferencesManager instance;
	
	private Preferences prefs;
	private Preferences connectPrefs;
	
	@SuppressWarnings("deprecation")
	private ConnectPreferencesManager() {
		prefs = (Preferences) new ConfigurationScope().getNode(OdysseusRCPPlugIn.PLUGIN_ID);
		connectPrefs = prefs.node("connect");
	}
	
	public static ConnectPreferencesManager getInstance() {
		if(instance == null) {
			instance = new ConnectPreferencesManager();
		}
		return instance;
	}
	
	public String getWsdlLocation() {
		return connectPrefs.get("wsdlLocation", "");
	}
	
	public String getService() {
		return connectPrefs.get("service", "");
	}
	
	public void setWdslLocation(String loc) {
		connectPrefs.put("wsdlLocation", loc);
	}
	
	public void setService(String service) {
		connectPrefs.put("service", service);
	}
	
	public boolean getAutoConnect() {
		try{
			return Boolean.valueOf(connectPrefs.get("autoconnect", "false"));
		} catch(Exception ex) {
			return false;
		}
	}
	
	public void setAutoConnect(boolean auto) {
		connectPrefs.put("autoconnect", String.valueOf(auto));
	}
	
	public void save() {
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

}
