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
package de.uniol.inf.is.odysseus.usermanagement.store.service.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.store.FileStore;
import de.uniol.inf.is.odysseus.core.server.store.IStore;
import de.uniol.inf.is.odysseus.core.server.store.MemoryStore;
import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractStoreDAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.usermanagement.store.service.domain.User;

public class UserDAO extends AbstractStoreDAO<User>{
	
	static Map<ITenant, UserDAO> userDAOs = new HashMap<>();

	static synchronized public UserDAO getInstance(ITenant tenant) throws IOException{
		UserDAO dao = userDAOs.get(tenant);
		if (dao == null){
			if (OdysseusConfiguration.instance.get("StoretypeUserMgmt")
					.equalsIgnoreCase("Filestore")) {
				dao = new UserDAO(new FileStore<String, User>(
						OdysseusConfiguration.instance.getFileProperty(
								"userStoreFilename", tenant.getName())),
						new ArrayList<User>());
			} else {
				dao = new UserDAO(new MemoryStore<String, User>(),
						new ArrayList<User>());
			}
			
			userDAOs.put(tenant, dao);
		}
		return dao;
	}
	
	public UserDAO(IStore<String, User> store, ArrayList<User> arrayList) {
		init(store, arrayList);
	}	
		
}
