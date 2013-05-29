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
package de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.store.FileStore;
import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractStoreDAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.User;

public class UserDAO extends AbstractStoreDAO<User>{
	
	static Map<ITenant, UserDAO> userDAOs = new HashMap<>();
	
	static synchronized public UserDAO getInstance(ITenant tenant) throws IOException{
		UserDAO userDAO = userDAOs.get(tenant);
		if (userDAO == null){
			userDAO = new UserDAO(tenant);
			userDAOs.put(tenant, userDAO);
		}
		return userDAO;
	}
		
	UserDAO(ITenant tenant) throws IOException {
		// FIXME: why here a tenant?!
		//super(new FileStore<String, User>(tenant.getName()+OdysseusConfiguration.get("userStoreFilename")), new ArrayList<User>());
		super(new FileStore<String, User>(OdysseusConfiguration.get("userStoreFilename")), new ArrayList<User>());
	}

}
