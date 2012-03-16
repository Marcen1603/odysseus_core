package de.uniol.inf.is.odysseus.usermanagement.filestore.service.impl;

import java.io.IOException;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractUserManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IGenericDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao.PrivilegeDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao.RoleDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao.UserDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.Privilege;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.Role;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.User;

public class UserManagementServiceImpl extends
		AbstractUserManagement<User, Role, Privilege> {

    private IGenericDAO<User, String> userDAO;
    private IGenericDAO<Role, String> roleDAO;
    private IGenericDAO<Privilege, String> privilegeDAO;
    
	@Override
	protected Role createEmptyRole() {
		return new Role();
	}

	@Override
	protected User createEmptyUser() {
		return new User();
	}

	@Override
	protected Privilege createEmptyPrivilege() {
		return new Privilege();
	}

	protected void activate(ComponentContext context) {
	    initDefaultUsers();
	}

	protected void deactivate(ComponentContext context) {

	}

    @Override
    protected IGenericDAO<User, String> getUserDAO() {
        try {
            if( userDAO == null ) {
                userDAO = UserDAO.getInstance();
                initDefaultUsers();
            }
            return userDAO;
        } catch (IOException e) {
            throw new RuntimeException("Could not get UserDAO", e);
        }
    }

    @Override
    protected IGenericDAO<Role, String> getRoleDAO() {
        try {
            if( roleDAO == null ) {
                roleDAO = RoleDAO.getInstance();
            }
            return roleDAO;
        } catch (IOException e) {
            throw new RuntimeException("Could not get RoleDAO", e);
        }
    }

    @Override
    protected IGenericDAO<Privilege, String> getPrivilegeDAO() {
        try {
            if( privilegeDAO == null ) {
                privilegeDAO = PrivilegeDAO.getInstance();
            }
            return privilegeDAO;
        } catch (IOException e) {
            throw new RuntimeException("Could not get PrivilegeDAO", e);
        }
    }

}
