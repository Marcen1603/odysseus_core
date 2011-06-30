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
package de.uniol.inf.is.odysseus.usermanagement.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.uniol.inf.is.odysseus.usermanagement.domain.Privilege;
import de.uniol.inf.is.odysseus.usermanagement.domain.Role;
import de.uniol.inf.is.odysseus.usermanagement.domain.User;
import de.uniol.inf.is.odysseus.usermanagement.domain.impl.PrivilegeImpl;
import de.uniol.inf.is.odysseus.usermanagement.domain.impl.RoleImpl;
import de.uniol.inf.is.odysseus.usermanagement.domain.impl.UserImpl;
import de.uniol.inf.is.odysseus.usermanagement.persistence.impl.PrivilegeDAO;
import de.uniol.inf.is.odysseus.usermanagement.persistence.impl.RoleDAO;
import de.uniol.inf.is.odysseus.usermanagement.persistence.impl.UserDAO;
import de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class UsermanagementServiceImpl implements UsermanagementService {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("odysseusPU");

    private final UserDAO userDAO = new UserDAO();
    private final RoleDAO roleDAO = new RoleDAO();
    private final PrivilegeDAO privilegeDAO = new PrivilegeDAO();

    public UsermanagementServiceImpl() {
        EntityManager em = entityManagerFactory.createEntityManager();
        this.userDAO.setEntityManager(em);
        this.roleDAO.setEntityManager(em);
        this.privilegeDAO.setEntityManager(em);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #createPrivilege(java.lang.String,
     * de.uniol.inf.is.odysseus.usermanagement.domain.User)
     */
    @Override
    public void createPrivilege(String objectURI, User caller) {
        PrivilegeImpl privilege = new PrivilegeImpl();
        privilegeDAO.create(privilege);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #removePrivilege
     * (de.uniol.inf.is.odysseus.usermanagement.domain.Privilege,
     * de.uniol.inf.is.odysseus.usermanagement.domain.User)
     */
    @Override
    public void removePrivilege(Privilege privilege, User caller) {
        PrivilegeImpl tmpPrivilege = privilegeDAO.find(privilege.getId());
        privilegeDAO.delete(tmpPrivilege);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #createRole(java.lang.String,
     * de.uniol.inf.is.odysseus.usermanagement.domain.User)
     */
    @Override
    public void createRole(String roleName, User caller) {
        RoleImpl role = new RoleImpl();
        role.setName(roleName);
        roleDAO.create(role);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #removeRole(de.uniol.inf.is.odysseus.usermanagement.domain.Role,
     * de.uniol.inf.is.odysseus.usermanagement.domain.User)
     */
    @Override
    public void removeRole(Role role, User caller) {
        RoleImpl tmpRole = roleDAO.find(role.getId());
        roleDAO.delete(tmpRole);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #createUser()
     */
    @Override
    public void createUser() {
        UserImpl user = new UserImpl();
        userDAO.create(user);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #activateUser(de.uniol.inf.is.odysseus.usermanagement.domain.User,
     * de.uniol.inf.is.odysseus.usermanagement.domain.User)
     */
    @Override
    public void activateUser(User user, User caller) {
        UserImpl tmpUser = userDAO.find(user.getId());
        tmpUser.setActive(true);
        userDAO.update(tmpUser);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #deactivateUser(de.uniol.inf.is.odysseus.usermanagement.domain.User,
     * de.uniol.inf.is.odysseus.usermanagement.domain.User)
     */
    @Override
    public void deactivateUser(User user, User caller) {
        UserImpl tmpUser = userDAO.find(user.getId());
        tmpUser.setActive(false);
        userDAO.update(tmpUser);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #removeUser(de.uniol.inf.is.odysseus.usermanagement.domain.User,
     * de.uniol.inf.is.odysseus.usermanagement.domain.User)
     */
    @Override
    public void removeUser(User user, User caller) {
        UserImpl tmpUser = userDAO.find(user.getId());
        userDAO.delete(tmpUser);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #findUser(java.lang.String,
     * de.uniol.inf.is.odysseus.usermanagement.domain.User)
     */
    @Override
    public void findUser(String username, User caller) {
        User user = userDAO.findByName(username);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #getUser(java.lang.String,
     * de.uniol.inf.is.odysseus.usermanagement.domain.User)
     */
    @Override
    public void getUser(String userId, User caller) {
        User user = userDAO.find(userId);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #getUsers()
     */
    @Override
    public void getUsers() {
        List<? extends User> users = userDAO.findAll();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #grantRole(de.uniol.inf.is.odysseus.usermanagement.domain.User,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Role,
     * de.uniol.inf.is.odysseus.usermanagement.domain.User)
     */
    @Override
    public void grantRole(User user, Role role, User caller) {
        UserImpl tmpUser = userDAO.find(user.getId());
        RoleImpl tmpRole = roleDAO.find(role.getId());
        tmpUser.addRole(tmpRole);
        userDAO.update(tmpUser);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #revokeRole(de.uniol.inf.is.odysseus.usermanagement.domain.User,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Role,
     * de.uniol.inf.is.odysseus.usermanagement.domain.User)
     */
    @Override
    public void revokeRole(User user, Role role, User caller) {
        UserImpl tmpUser = userDAO.find(user.getId());
        RoleImpl tmpRole = roleDAO.find(role.getId());
        tmpUser.removeRole(tmpRole);
        userDAO.update(tmpUser);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #grantPermission()
     */
    @Override
    public void grantPermission(Role role, String objectURI, User caller) {
        RoleImpl tmpRole = roleDAO.find(role.getId());

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #revokePermission(de.uniol.inf.is.odysseus.usermanagement.domain.Role,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Privilege,
     * de.uniol.inf.is.odysseus.usermanagement.domain.User)
     */
    @Override
    public void revokePermission(Role role, Privilege privilege, User caller) {
        // TODO Auto-generated method stub
        throw new NotImplementedException();
    }

}
