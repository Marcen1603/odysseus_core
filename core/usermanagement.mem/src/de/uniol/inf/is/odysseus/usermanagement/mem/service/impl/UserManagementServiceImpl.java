package de.uniol.inf.is.odysseus.usermanagement.mem.service.impl;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractUserManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IGenericDAO;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.dao.PrivilegeDAO;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.dao.RoleDAO;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.dao.UserDAO;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.domain.Privilege;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.domain.Role;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.domain.User;

public class UserManagementServiceImpl extends AbstractUserManagement<User, Role, Privilege> {

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
        if (userDAO == null) {
            userDAO = UserDAO.getInstance();
            initDefaultUsers();
        }
        return userDAO;
    }

    @Override
    protected IGenericDAO<Role, String> getRoleDAO() {
        if (roleDAO == null) {
            roleDAO = RoleDAO.getInstance();
        }
        return roleDAO;
    }

    @Override
    protected IGenericDAO<Privilege, String> getPrivilegeDAO() {
        if (privilegeDAO == null) {
            privilegeDAO = PrivilegeDAO.getInstance();
        }
        return privilegeDAO;
    }

}
