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

public class UserManagementProvider {
	
	Logger logger = LoggerFactory.getLogger(UserManagementProvider.class);

	static private Map<String, IUserManagement> usrMgmt = new HashMap<>();
	
	
	static synchronized public IUserManagement getUsermanagement() {
		IUserManagement ret = usrMgmt.get(OdysseusConfiguration.get("StoretypeUserMgmt"));
		while (ret == null){
			try {
				UserManagementProvider.class.wait(500);
			} catch (InterruptedException e) {
			}
		}
		if (!ret.isInitialized()){
			ret.initialize();
		}
		return ret;
	}
	
	static public ISessionManagement getSessionmanagement() {
		ISessionManagement ret = getUsermanagement().getSessionManagement();
		return ret;
	}
	
	protected void bindUserManagement(IUserManagement usermanagement) {
		if (usrMgmt.get(usermanagement.getType()) == null){
			usrMgmt.put(usermanagement.getType(), usermanagement);
			logger.debug("Bound UserManagementService "+usermanagement.getType());
		}else{
			throw new RuntimeException("UserManagement "+usermanagement.getType()+" already bound!");
		}
		synchronized(UserManagementProvider.class){
			UserManagementProvider.class.notifyAll();
		}
	}

	protected void unbindUserManagement(IUserManagement usermanagement) {
		if (usrMgmt.get(usermanagement.getType())!= null){
			usrMgmt.remove(usermanagement.getType());
			logger.debug("User management "+usermanagement.getType()+" removed");
		}else{
			throw new RuntimeException("UserManagement "+usermanagement.getType()+" not bound!");
		}
	}
	
	
}
