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

import de.uniol.inf.is.odysseus.usermanagement.domain.Privilege;
import de.uniol.inf.is.odysseus.usermanagement.domain.Role;
import de.uniol.inf.is.odysseus.usermanagement.domain.User;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * TODO Move to base bundle
 */
public interface UsermanagementService {
    void createPrivilege(String objectURI, User caller);

    void removePrivilege(Privilege privilege, User caller);

    void createRole(String roleName, User caller);

    void removeRole(Role role, User caller);

    void createUser();

    void activateUser(User user, User caller);

    void deactivateUser(User user, User caller);

    void removeUser(User user, User caller);

    void findUser(String username, User caller);

    void getUser(String userId, User caller);

    void getUsers();

    void grantRole(User user, Role role, User caller);

    void revokeRole(User user, Role role, User caller);

    void grantPermission(Role role, String objectURI, User caller);

    void revokePermission(Role role, Privilege privilege, User caller);
}
