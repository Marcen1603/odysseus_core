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

import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguration;

public class LoginPreferencesManager {

	private static LoginPreferencesManager instance;
	
	private LoginPreferencesManager() {
	}
	
	public static LoginPreferencesManager getInstance() {
		if( instance == null ) 
			instance = new LoginPreferencesManager();
		return instance;
	}
	
	public String getUsername() {
		return OdysseusRCPConfiguration.get("username", "");
	}
	
	public String getPassword() {
		return OdysseusRCPConfiguration.get("password", "");
	}
	
	public String getTenant(){
		return OdysseusRCPConfiguration.get("tenant", "");
	}
	
	public void setUsername( String username ) {
		OdysseusRCPConfiguration.set("username", username);
	}
	
	public void setPassword( String password ) {
		OdysseusRCPConfiguration.set("password", password);
	}
	
	public void setTenant(String tenant){
		OdysseusRCPConfiguration.set("tenant", tenant);
	}

	public boolean getAutoLogin() {
		try {
			return Boolean.valueOf(OdysseusRCPConfiguration.get("autologin", "false"));
		} catch( Exception ex ) {
			return false;
		}
	}
	
	public void setAutoLogin( boolean autoLogin ) {
		OdysseusRCPConfiguration.set("autologin", String.valueOf(autoLogin));
	}
	
	public void save() {
		OdysseusRCPConfiguration.save();
	}
}
