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
package de.uniol.inf.is.odysseus.usermanagement.filestore.service.impl;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractSessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IGenericDAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao.TenantDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao.UserDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.Tenant;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.User;

public class SessionManagementServiceImpl extends AbstractSessionManagement<User, Tenant>{

	@Override
	protected IGenericDAO<Tenant, String> getTenantDAO() {
		try {
			return TenantDAO.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected IGenericDAO<User, String> getUserDAO(ITenant tenant) {
		try {
			return UserDAO.getInstance(tenant);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	} 
	
	
	
}
