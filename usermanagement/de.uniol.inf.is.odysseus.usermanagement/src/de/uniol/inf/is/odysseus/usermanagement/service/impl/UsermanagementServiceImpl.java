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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.uniol.inf.is.odysseus.usermanagement.domain.Permission;
import de.uniol.inf.is.odysseus.usermanagement.domain.Privilege;
import de.uniol.inf.is.odysseus.usermanagement.domain.Role;
import de.uniol.inf.is.odysseus.usermanagement.domain.Session;
import de.uniol.inf.is.odysseus.usermanagement.domain.User;
import de.uniol.inf.is.odysseus.usermanagement.domain.impl.PrivilegeImpl;
import de.uniol.inf.is.odysseus.usermanagement.domain.impl.RoleImpl;
import de.uniol.inf.is.odysseus.usermanagement.domain.impl.SessionImpl;
import de.uniol.inf.is.odysseus.usermanagement.domain.impl.UserImpl;
import de.uniol.inf.is.odysseus.usermanagement.domain.impl.UsermanagementPermission;
import de.uniol.inf.is.odysseus.usermanagement.persistence.impl.PrivilegeDAO;
import de.uniol.inf.is.odysseus.usermanagement.persistence.impl.RoleDAO;
import de.uniol.inf.is.odysseus.usermanagement.persistence.impl.UserDAO;
import de.uniol.inf.is.odysseus.usermanagement.policy.ChangePasswordPolicy;
import de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class UsermanagementServiceImpl implements UsermanagementService {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("odysseusPU");
    private final static String OBJECT_URI = "Usermanagement";
    private final UserDAO userDAO = new UserDAO();
    private final RoleDAO roleDAO = new RoleDAO();
    private final PrivilegeDAO privilegeDAO = new PrivilegeDAO();
    private final SessionStore sessionStore = SessionStore.getInstance();

    public UsermanagementServiceImpl() {
        final EntityManager em = this.entityManagerFactory.createEntityManager();
        this.userDAO.setEntityManager(em);
        this.roleDAO.setEntityManager(em);
        this.privilegeDAO.setEntityManager(em);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #createRole(java.lang.String,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public Role createRole(final String name, final Session caller) {
        if (this.hasPermission(caller, UsermanagementPermission.CREATE_ROLE, UsermanagementServiceImpl.OBJECT_URI)) {
            final RoleImpl role = new RoleImpl();
            role.setName(name);
            this.roleDAO.create(role);
            return role;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #deleteRole(de.uniol.inf.is.odysseus.usermanagement.domain.Role,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public void deleteRole(final Role role, final Session caller) {
        if (this.hasPermission(caller, UsermanagementPermission.DELETE_ROLE, UsermanagementServiceImpl.OBJECT_URI)) {
            final RoleImpl tmpRole = this.roleDAO.find(role.getId());
            if (tmpRole != null) {
                this.roleDAO.delete(tmpRole);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #findRole(java.lang.String,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public Role findRole(final String name, final Session caller) {
        final RoleImpl role = this.roleDAO.findByName(name);
        return role;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #getRole(java.lang.String,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public Role getRole(final String roleId, final Session caller) {
        final RoleImpl role = this.roleDAO.find(roleId);
        return role;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #getRoles(de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public List<? extends Role> getRoles(final Session caller) {
        if (this.hasPermission(caller, UsermanagementPermission.GET_ALL_USER, UsermanagementServiceImpl.OBJECT_URI)
            || this.hasPermission(caller, UsermanagementPermission.GET_ALL, UsermanagementServiceImpl.OBJECT_URI)) {
            final List<RoleImpl> roles = this.roleDAO.findAll();
            return roles;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #createUser(java.lang.String,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public User createUser(final String name, final Session caller) {
        if (this.hasPermission(caller, UsermanagementPermission.CREATE_USER, UsermanagementServiceImpl.OBJECT_URI)) {
            final UserImpl user = new UserImpl();
            user.setName(name);
            this.userDAO.create(user);
            return user;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #changePassword(de.uniol.inf.is.odysseus.usermanagement.domain.User,
     * byte[], de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public void changePassword(final User user, final byte[] password, final Session caller) {
        final Session session = this.sessionStore.get(caller.getId());
        if (ChangePasswordPolicy.allow(user, session.getUser()) || this.hasPermission(session, UsermanagementPermission.ALTER_USER, UsermanagementServiceImpl.OBJECT_URI)) {
            final UserImpl tmpUser = this.userDAO.find(user.getId());
            tmpUser.setPassword(password);
            this.userDAO.update(tmpUser);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #activateUser(de.uniol.inf.is.odysseus.usermanagement.domain.User,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public void activateUser(final User user, final Session caller) {
        if (this.hasPermission(caller, UsermanagementPermission.ALTER_USER, UsermanagementServiceImpl.OBJECT_URI)) {
            final UserImpl tmpUser = this.userDAO.find(user.getId());
            tmpUser.setActive(true);
            this.userDAO.update(tmpUser);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #deactivateUser(de.uniol.inf.is.odysseus.usermanagement.domain.User,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public void deactivateUser(final User user, final Session caller) {
        if (this.hasPermission(caller, UsermanagementPermission.DEACTIVATE_USER, UsermanagementServiceImpl.OBJECT_URI)) {
            final UserImpl tmpUser = this.userDAO.find(user.getId());
            tmpUser.setActive(false);
            this.userDAO.update(tmpUser);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #deleteUser(de.uniol.inf.is.odysseus.usermanagement.domain.User,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public void deleteUser(final User user, final Session caller) {
        if (this.hasPermission(caller, UsermanagementPermission.DELETE_USER, UsermanagementServiceImpl.OBJECT_URI)) {
            final UserImpl tmpUser = this.userDAO.find(user.getId());
            this.userDAO.delete(tmpUser);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #findUser(java.lang.String,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public User findUser(final String name, final Session caller) {
        final UserImpl user = this.userDAO.findByName(name);
        return user;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #getUser(java.lang.String,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public User getUser(final String userId, final Session caller) {
        final User user = this.userDAO.find(userId);
        return user;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #getUsers(de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public List<? extends User> getUsers(final Session caller) {
        if (this.hasPermission(caller, UsermanagementPermission.GET_ALL_USER, UsermanagementServiceImpl.OBJECT_URI)
            || this.hasPermission(caller, UsermanagementPermission.GET_ALL, UsermanagementServiceImpl.OBJECT_URI)) {
            final List<UserImpl> users = this.userDAO.findAll();
            return users;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #grantRole(de.uniol.inf.is.odysseus.usermanagement.domain.User,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Role,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public void grantRole(final User user, final Role role, final Session caller) {
        if (this.hasPermission(caller, UsermanagementPermission.GRANT_ROLE, UsermanagementServiceImpl.OBJECT_URI)) {
            final UserImpl tmpUser = this.userDAO.find(user.getId());
            final RoleImpl tmpRole = this.roleDAO.find(role.getId());
            tmpUser.addRole(tmpRole);
            this.userDAO.update(tmpUser);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #revokeRole(de.uniol.inf.is.odysseus.usermanagement.domain.User,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Role,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public void revokeRole(final User user, final Role role, final Session caller) {
        if (this.hasPermission(caller, UsermanagementPermission.REVOKE_ROLE, UsermanagementServiceImpl.OBJECT_URI)) {
            final UserImpl tmpUser = this.userDAO.find(user.getId());
            final RoleImpl tmpRole = this.roleDAO.find(role.getId());
            tmpUser.removeRole(tmpRole);
            this.userDAO.update(tmpUser);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #grantPermission(de.uniol.inf.is.odysseus.usermanagement.domain.User,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Permission,
     * java.lang.String, de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public void grantPermission(final User user, final Permission permission, final String objectURI, final Session caller) {
        final Set<Permission> permissions = new HashSet<Permission>();
        permissions.add(permission);
        this.grantPermissions(user, permissions, objectURI, caller);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #grantPermissions(de.uniol.inf.is.odysseus.usermanagement.domain.User,
     * java.util.Set, java.lang.String,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public void grantPermissions(final User user, final Set<Permission> permissions, final String objectURI, final Session caller) {
        if (this.hasPermission(caller, UsermanagementPermission.GRANT, UsermanagementServiceImpl.OBJECT_URI)) {
            boolean create = true;
            for (final Privilege privilege : user.getPrivileges()) {
                if (privilege.getObjectURI().equals(objectURI)) {
                    final PrivilegeImpl tmpPrivilege = this.privilegeDAO.find(privilege.getId());
                    if (tmpPrivilege != null) {
                        create = false;
                        for (final Permission permission : permissions) {
                            tmpPrivilege.addPermission(permission);
                        }
                        this.privilegeDAO.update(tmpPrivilege);
                    }
                }
            }
            if (create) {
                final PrivilegeImpl tmpPrivilege = new PrivilegeImpl();
                tmpPrivilege.setObjectURI(objectURI);
                for (final Permission permission : permissions) {
                    tmpPrivilege.addPermission(permission);
                }
                this.privilegeDAO.create(tmpPrivilege);
                final UserImpl tmpUser = this.userDAO.find(user.getId());
                tmpUser.addPrivilege(tmpPrivilege);

            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #grantPermission(de.uniol.inf.is.odysseus.usermanagement.domain.Role,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Permission,
     * java.lang.String, de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public void grantPermission(final Role role, final Permission permission, final String objectURI, final Session caller) {
        final Set<Permission> permissions = new HashSet<Permission>();
        permissions.add(permission);
        this.grantPermissions(role, permissions, objectURI, caller);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #grantPermissions(de.uniol.inf.is.odysseus.usermanagement.domain.Role,
     * java.util.Set, java.lang.String,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public void grantPermissions(final Role role, final Set<Permission> permissions, final String objectURI, final Session caller) {
        if (this.hasPermission(caller, UsermanagementPermission.GRANT, UsermanagementServiceImpl.OBJECT_URI)) {
            boolean create = true;
            for (final Privilege privilege : role.getPrivileges()) {
                if (privilege.getObjectURI().equals(objectURI)) {
                    final PrivilegeImpl tmpPrivilege = this.privilegeDAO.find(privilege.getId());
                    if (tmpPrivilege != null) {
                        create = false;
                        for (final Permission permission : permissions) {
                            tmpPrivilege.addPermission(permission);
                        }
                        this.privilegeDAO.update(tmpPrivilege);
                    }
                }
            }
            if (create) {
                final PrivilegeImpl tmpPrivilege = new PrivilegeImpl();
                tmpPrivilege.setObjectURI(objectURI);
                for (final Permission permission : permissions) {
                    tmpPrivilege.addPermission(permission);
                }
                this.privilegeDAO.create(tmpPrivilege);
                final RoleImpl tmpRole = this.roleDAO.find(role.getId());
                tmpRole.addPrivilege(tmpPrivilege);

            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #revokePermission(de.uniol.inf.is.odysseus.usermanagement.domain.User,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Permission,
     * java.lang.String, de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public void revokePermission(final User user, final Permission permission, final String objectURI, final Session caller) {
        final Set<Permission> permissions = new HashSet<Permission>();
        permissions.add(permission);
        this.revokePermissions(user, permissions, objectURI, caller);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #revokePermissions(de.uniol.inf.is.odysseus.usermanagement.domain.User,
     * java.util.Set, java.lang.String,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public void revokePermissions(final User user, final Set<Permission> permissions, final String objectURI, final Session caller) {
        if (this.hasPermission(caller, UsermanagementPermission.REVOKE, UsermanagementServiceImpl.OBJECT_URI)) {
            for (final Privilege privilege : user.getPrivileges()) {
                if (privilege.getObjectURI().equals(objectURI)) {
                    final PrivilegeImpl tmpPrivilege = this.privilegeDAO.find(privilege.getId());
                    if (tmpPrivilege != null) {
                        for (final Permission permission : permissions) {
                            tmpPrivilege.removePermission(permission);
                        }
                        this.privilegeDAO.update(tmpPrivilege);
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #revokePermission(de.uniol.inf.is.odysseus.usermanagement.domain.Role,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Permission,
     * java.lang.String, de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public void revokePermission(final Role role, final Permission permission, final String objectURI, final Session caller) {
        final Set<Permission> permissions = new HashSet<Permission>();
        permissions.add(permission);
        this.revokePermissions(role, permissions, objectURI, caller);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
     * #revokePermissions(de.uniol.inf.is.odysseus.usermanagement.domain.Role,
     * java.util.Set, java.lang.String,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public void revokePermissions(final Role role, final Set<Permission> permissions, final String objectURI, final Session caller) {
        if (this.hasPermission(caller, UsermanagementPermission.REVOKE, UsermanagementServiceImpl.OBJECT_URI)) {
            for (final Privilege privilege : role.getPrivileges()) {
                if (privilege.getObjectURI().equals(objectURI)) {
                    final PrivilegeImpl tmpPrivilege = this.privilegeDAO.find(privilege.getId());
                    if (tmpPrivilege != null) {
                        for (final Permission permission : permissions) {
                            tmpPrivilege.removePermission(permission);
                        }
                        this.privilegeDAO.update(tmpPrivilege);
                    }
                }
            }
        }
    }

    @Override
    public boolean hasPermission(final Session session, final Permission permission, final String objectURI) {
        final Map<String, Set<Permission>> permissions = new HashMap<String, Set<Permission>>();
        final SessionImpl tmpSession = this.sessionStore.get(session.getId());
        if (tmpSession.isValid()) {
            final User user = tmpSession.getUser();
            for (final Privilege privilege : user.getPrivileges()) {
                if (!permissions.containsKey(privilege.getObjectURI())) {
                    permissions.put(privilege.getObjectURI(), new HashSet<Permission>());
                }
                permissions.get(privilege.getObjectURI()).addAll(privilege.getPermissions());
            }
            for (final Role role : user.getRoles()) {
                for (final Privilege privilege : role.getPrivileges()) {
                    if (!permissions.containsKey(privilege.getObjectURI())) {
                        permissions.put(privilege.getObjectURI(), new HashSet<Permission>());
                    }
                    permissions.get(privilege.getObjectURI()).addAll(privilege.getPermissions());
                }
            }
            tmpSession.updateSession();
            return permissions.containsKey(objectURI) && permissions.get(objectURI).contains(permission);
        }
        return false;
    }
}
