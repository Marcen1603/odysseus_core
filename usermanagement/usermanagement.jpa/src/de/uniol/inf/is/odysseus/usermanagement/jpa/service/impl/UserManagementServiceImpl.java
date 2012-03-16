/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.usermanagement.jpa.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractUserManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IGenericDAO;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagement;
import de.uniol.inf.is.odysseus.usermanagement.jpa.domain.impl.PrivilegeImpl;
import de.uniol.inf.is.odysseus.usermanagement.jpa.domain.impl.RoleImpl;
import de.uniol.inf.is.odysseus.usermanagement.jpa.domain.impl.UserImpl;
import de.uniol.inf.is.odysseus.usermanagement.jpa.persistence.impl.PrivilegeDAO;
import de.uniol.inf.is.odysseus.usermanagement.jpa.persistence.impl.RoleDAO;
import de.uniol.inf.is.odysseus.usermanagement.jpa.persistence.impl.UserDAO;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class UserManagementServiceImpl extends AbstractUserManagement<UserImpl,RoleImpl,PrivilegeImpl> implements IUserManagement {
	
    private EntityManagerFactory entityManagerFactory;
    private EntityManager em;
    
	private UserDAO userDAO;
	private RoleDAO roleDAO;
	private PrivilegeDAO privDAO;
	
	@Override
	protected UserImpl createEmptyUser() {
		return new UserImpl();
	}
	
	@Override
	protected RoleImpl createEmptyRole() {
		return new RoleImpl();
	}
	
	@Override
	protected PrivilegeImpl createEmptyPrivilege(){
		return new PrivilegeImpl();
	}

	protected void activate(ComponentContext context) {
		entityManagerFactory = Persistence.createEntityManagerFactory("odysseusPU");
		em = entityManagerFactory.createEntityManager();
		initDefaultUsers();
	}

	protected void deactivate(ComponentContext context) {

	}

    @Override
    protected IGenericDAO<UserImpl, String> getUserDAO() {
        if( userDAO == null ) {
            userDAO = new UserDAO();
            userDAO.setEntityManager(em);
        }
        return userDAO;
    }

    @Override
    protected IGenericDAO<RoleImpl, String> getRoleDAO() {
        if( roleDAO == null ) {
            roleDAO = new RoleDAO();
            roleDAO.setEntityManager(em);
        }
        return roleDAO;
    }

    @Override
    protected IGenericDAO<PrivilegeImpl, String> getPrivilegeDAO() {
        if( privDAO == null ) {
            privDAO = new PrivilegeDAO();
            privDAO.setEntityManager(em);
        }
        return privDAO;
    }

}
