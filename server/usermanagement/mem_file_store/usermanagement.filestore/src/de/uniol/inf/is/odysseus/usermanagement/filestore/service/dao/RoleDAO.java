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
import de.uniol.inf.is.odysseus.core.server.store.IStore;
import de.uniol.inf.is.odysseus.core.server.store.MemoryStore;
import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractStoreDAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.Role;

public class RoleDAO extends AbstractStoreDAO<Role>{
	
	static Map<ITenant,RoleDAO> daos = new HashMap<>();
	
	static synchronized public RoleDAO getInstance(ITenant tenant) throws IOException{
		RoleDAO dao = daos.get(tenant);
		if (dao == null){
			if (OdysseusConfiguration.instance.get("StoretypeUserMgmt")
					.equalsIgnoreCase("Filestore")) {
				dao = new RoleDAO(new FileStore<String, Role>(
						OdysseusConfiguration.instance.getFileProperty(
								"roleStoreFilename", tenant.getName())),
						new ArrayList<Role>());
			} else {
				dao = new RoleDAO(new MemoryStore<String, Role>(),
						new ArrayList<Role>());
			}
			daos.put(tenant, dao);
		}
		return dao;
	}

	public RoleDAO(IStore<String, Role> store,
			ArrayList<Role> arrayList) {
		init(store, arrayList);
	}


}
