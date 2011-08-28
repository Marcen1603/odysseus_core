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
package de.uniol.inf.is.odysseus.usermanagement.service;

import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.usermanagement.domain.Permission;
import de.uniol.inf.is.odysseus.usermanagement.domain.Role;
import de.uniol.inf.is.odysseus.usermanagement.domain.Session;
import de.uniol.inf.is.odysseus.usermanagement.domain.User;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *         TODO Move to base bundle
 */
public interface UsermanagementService {
    /**
     * @param name
     * @param caller
     * @return
     */
    Role createRole(String name, Session caller);

    /**
     * @param role
     * @param caller
     */
    void deleteRole(Role role, Session caller);

    /**
     * @param name
     * @param caller
     * @return
     */
    Role findRole(String name, Session caller);

    /**
     * @param roleId
     * @param caller
     * @return
     */
    Role getRole(String roleId, Session caller);

    /**
     * @param caller
     * @return
     */
    List<? extends Role> getRoles(Session caller);

    /**
     * @param name
     * @param caller
     * @return
     */
    User createUser(String name, Session caller);

    /**
     * @param user
     * @param password
     * @param caller
     */
    void changePassword(User user, byte[] password, Session caller);

    /**
     * @param user
     * @param caller
     */
    void activateUser(User user, Session caller);

    /**
     * @param user
     * @param caller
     */
    void deactivateUser(User user, Session caller);

    /**
     * @param user
     * @param caller
     */
    void deleteUser(User user, Session caller);

    /**
     * @param name
     * @param caller
     * @return
     */
    User findUser(String name, Session caller);

    /**
     * @param userId
     * @param caller
     * @return
     */
    User getUser(String userId, Session caller);

    /**
     * @param caller
     * @return
     */
    List<? extends User> getUsers(Session caller);

    /**
     * @param user
     * @param role
     * @param caller
     */
    void grantRole(User user, Role role, Session caller);

    /**
     * @param user
     * @param role
     * @param caller
     */
    void revokeRole(User user, Role role, Session caller);

    /**
     * @param user
     * @param permission
     * @param objectURI
     * @param caller
     */
    void grantPermission(User user, Permission permission, String objectURI, Session caller);

    /**
     * @param user
     * @param permissions
     * @param objectURI
     * @param caller
     */
    void grantPermissions(User user, Set<Permission> permissions, String objectURI, Session caller);

    /**
     * @param role
     * @param permission
     * @param objectURI
     * @param caller
     */
    void grantPermission(Role role, Permission permission, String objectURI, Session caller);

    /**
     * @param role
     * @param permissions
     * @param objectURI
     * @param caller
     */
    void grantPermissions(Role role, Set<Permission> permissions, String objectURI, Session caller);

    /**
     * @param user
     * @param permission
     * @param objectURI
     * @param caller
     */
    void revokePermission(User user, Permission permission, String objectURI, Session caller);

    /**
     * @param user
     * @param permissions
     * @param objectURI
     * @param caller
     */
    void revokePermissions(User user, Set<Permission> permissions, String objectURI, Session caller);

    /**
     * @param role
     * @param permission
     * @param objectURI
     * @param caller
     */
    void revokePermission(Role role, Permission permission, String objectURI, Session caller);

    /**
     * @param role
     * @param permissions
     * @param objectURI
     * @param caller
     */
    void revokePermissions(Role role, Set<Permission> permissions, String objectURI, Session caller);

    /**
     * @param caller
     * @param permission
     * @param objectURI
     * @return
     */
    boolean hasPermission(Session caller, Permission permission, String objectURI);
}
