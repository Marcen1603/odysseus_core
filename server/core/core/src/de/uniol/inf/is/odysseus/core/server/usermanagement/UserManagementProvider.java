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
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;

public class UserManagementProvider {

	private static UserManagementProvider instance;

	static Logger logger = LoggerFactory.getLogger(UserManagementProvider.class);

	// Preload daos
	@SuppressWarnings("unused")
	static private TenantDAO dao = TenantDAO.getInstance();

	private static String defaultTenantName = "";
	
	public UserManagementProvider() {
		instance = this;
	}

	private Map<String, IUserManagement> usrMgmt = new HashMap<>();
	

	static synchronized public ITenant getDefaultTenant() {
		return getTenant(defaultTenantName);
	}

	static public synchronized ITenant getTenant(String name) {
		return TenantDAO.getInstance().findByName(name);
	}

	static public synchronized ITenant createNewTenant(String name, ISession caller) {
		if (waitForUsermanagement().hasPermission(caller, UserManagementPermission.CREATE_TENANT,
				UserManagementPermission.objectURI)) {

			ITenant t = new Tenant();
			t.setName(name);
			t = TenantDAO.getInstance().create(t);
			return t;
		}
		throw new PermissionException("Not right to create tenant");

	}

	static public synchronized List<ITenant> getTenants() {
		return TenantDAO.getInstance().findAll();
	}

	/**
	 * Get the current user management.
	 * 
	 * @param wait
	 *            if not bound, wait for user management
	 * @return the user management, could be null if wait is false
	 */
	static public IUserManagement getUsermanagement(boolean wait) {
		if (wait) {
			return waitForUsermanagement();
		} else {
			return instance.usrMgmt.get(OdysseusConfiguration.get("StoretypeUserMgmt").toLowerCase());
		}
	}

	@Deprecated
	static public IUserManagement getUsermanagent() {
		return getUsermanagement(true);
	}

	static synchronized private IUserManagement waitForUsermanagement() {
		IUserManagement ret = instance.usrMgmt.get(OdysseusConfiguration.get("StoretypeUserMgmt").toLowerCase());
		while (ret == null) {
			try {
				UserManagementProvider.class.wait(1000);
				logger.debug("Waiting for UserManagement " + OdysseusConfiguration.get("StoretypeUserMgmt"));
				ret = instance.usrMgmt.get(OdysseusConfiguration.get("StoretypeUserMgmt").toLowerCase());
			} catch (InterruptedException e) {
			}
		}

		for (ITenant t : TenantDAO.getInstance().allEntities) {

			if (!ret.isInitialized(t)) {
				ret.initialize(t);
			}
		}
		return ret;
	}

	static public ISessionManagement getSessionmanagement() {
		ISessionManagement ret = waitForUsermanagement().getSessionManagement();
		return ret;
	}

	protected void bindUserManagement(IUserManagement usermanagement) {
		if (usrMgmt.get(usermanagement.getType().toLowerCase()) == null) {
			usrMgmt.put(usermanagement.getType().toLowerCase(), usermanagement);
			logger.debug("Bound UserManagementService " + usermanagement.getType());
		} else {
			throw new RuntimeException("UserManagement " + usermanagement.getType() + " already bound!");
		}
		synchronized (UserManagementProvider.class) {
			UserManagementProvider.class.notifyAll();
		}
	}

	protected void unbindUserManagement(IUserManagement usermanagement) {
		if (usrMgmt.get(usermanagement.getType().toLowerCase()) != null) {
			usrMgmt.remove(usermanagement.getType().toLowerCase());
			logger.debug("User management " + usermanagement.getType() + " removed");
		} else {
			throw new RuntimeException("UserManagement " + usermanagement.getType() + " not bound!");
		}
	}

}
