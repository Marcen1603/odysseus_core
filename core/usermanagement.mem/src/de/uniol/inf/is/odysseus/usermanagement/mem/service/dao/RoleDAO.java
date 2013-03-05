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
package de.uniol.inf.is.odysseus.usermanagement.mem.service.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.store.MemoryStore;
import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractStoreDAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.domain.Role;

public class RoleDAO extends AbstractStoreDAO<Role>{
	
	static Map<ITenant,RoleDAO> daos = new HashMap<>();
	
	static synchronized public RoleDAO getInstance(ITenant tenant){
		RoleDAO dao = daos.get(tenant);
		if (dao == null){
			dao = new RoleDAO(tenant);
			daos.put(tenant, dao);
		}
		return dao;
	}
	
	RoleDAO(ITenant tenant) {
		super(new MemoryStore<String, Role>(), new ArrayList<Role>());
	}

}
