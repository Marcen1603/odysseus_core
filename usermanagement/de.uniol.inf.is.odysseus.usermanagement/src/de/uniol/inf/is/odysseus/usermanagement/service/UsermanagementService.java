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

    Role createRole(String name, Session caller);

    void deleteRole(Role role, Session caller);

    Role findRole(String name, Session caller);

    Role getRole(String roleId, Session caller);

    List<? extends Role> getRoles(Session caller);

    User createUser(String name, Session caller);

    void changePassword(User user, byte[] password, Session caller);

    void activateUser(User user, Session caller);

    void deactivateUser(User user, Session caller);

    void deleteUser(User user, Session caller);

    User findUser(String name, Session caller);

    User getUser(String userId, Session caller);

    List<? extends User> getUsers(Session caller);

    void grantRole(User user, Role role, Session caller);

    void revokeRole(User user, Role role, Session caller);

    void grantPermission(User user, Permission permission, String objectURI, Session caller);

    void grantPermissions(User user, Set<Permission> permissions, String objectURI, Session caller);

    void grantPermission(Role role, Permission permission, String objectURI, Session caller);

    void grantPermissions(Role role, Set<Permission> permissions, String objectURI, Session caller);

    void revokePermission(User user, Permission permission, String objectURI, Session caller);

    void revokePermissions(User user, Set<Permission> permissions, String objectURI, Session caller);

    void revokePermission(Role role, Permission permission, String objectURI, Session caller);

    void revokePermissions(Role role, Set<Permission> permissions, String objectURI, Session caller);

    boolean hasPermission(Session caller, Permission permission, String objectURI);
}
