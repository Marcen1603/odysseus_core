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
package de.uniol.inf.is.odysseus.core.server.usermanagement;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;



public class UserManagement {
	
	Logger logger = LoggerFactory.getLogger(UserManagement.class);

	static private Map<String, IUserManagement> usrMgmt = new HashMap<>();
	static private Map<String,ISessionManagement> sessMgmt = new HashMap<>();
	
	
	static public IUserManagement getUsermanagement() {
		IUserManagement ret = usrMgmt.get(OdysseusConfiguration.get("StoretypeUserMgmt"));
		if (!ret.isInitialized()){
			ret.initialize();
		}
		return ret;
	}
	
	static public ISessionManagement getSessionmanagement() {
		// Init User Management
		getUsermanagement();
		ISessionManagement ret = sessMgmt.get(OdysseusConfiguration.get("StoretypeUserMgmt"));
		return ret;
	}
	
	protected void bindUserManagement(IUserManagement usermanagement) {
		if (usrMgmt.get(usermanagement.getType()) == null){
			usrMgmt.put(usermanagement.getType(), usermanagement);
			logger.debug("Bound UserManagementService "+usermanagement.getType());
		}else{
			throw new RuntimeException("UserManagement "+usermanagement.getType()+" already bound!");
		}
	}

	// TODO: Will man das?
	protected void unbindUserManagement(IUserManagement usermanagement) {
		usermanagement = null;
	}

	protected void bindSessionManagement(ISessionManagement sessionmanagement) {
		if (sessMgmt.get(sessionmanagement.getType()) == null){
			sessMgmt.put(sessionmanagement.getType(),sessionmanagement);
			logger.debug("Bound SessionManagementService "+sessionmanagement.getType());
		}else{
			throw new RuntimeException("SessionManagement already bound!");
		}
	}

	protected void unbindSessionManagement(ISessionManagement sessionmanagement) {
		sessionmanagement = null;
	}
	
	
}
