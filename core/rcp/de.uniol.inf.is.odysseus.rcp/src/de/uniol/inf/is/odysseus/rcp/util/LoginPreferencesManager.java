/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class LoginPreferencesManager {

	private static LoginPreferencesManager instance;
	
	private Preferences prefs;
	private Preferences loginPrefs;
	
	@SuppressWarnings("deprecation")
	private LoginPreferencesManager() {
		prefs = new ConfigurationScope().getNode(OdysseusRCPPlugIn.PLUGIN_ID);
		loginPrefs = prefs.node("login");
	}
	
	public static LoginPreferencesManager getInstance() {
		if( instance == null ) 
			instance = new LoginPreferencesManager();
		return instance;
	}
	
	public String getUsername() {
		return loginPrefs.get("username", "");
	}
	
	public String getPassword() {
		return loginPrefs.get("password", "");
	}
	
	public void setUsername( String username ) {
		loginPrefs.put("username", username);
	}
	
	public void setPassword( String password ) {
		// TODO: Verschlüsselung für das Passwort
		loginPrefs.put("password", password);
	}
	
	public boolean getAutoLogin() {
		try {
			return Boolean.valueOf(loginPrefs.get("autologin", "false"));
		} catch( Exception ex ) {
			return false;
		}
	}
	
	public void setAutoLogin( boolean autoLogin ) {
		loginPrefs.put("autologin", String.valueOf(autoLogin));
	}
	
	public void save() {
		try {
			// Forces the application to save the preferences
			prefs.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
}
