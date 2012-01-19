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
package de.uniol.inf.is.odysseus.usermanagement;

import java.util.List;
import java.util.Set;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *         
 */
public interface IUserManagement {
    /**
     * @param name
     * @param caller
     * @return
     */
    IRole createRole(String name, ISession caller);

    /**
     * @param role
     * @param caller
     */
    void deleteRole(IRole role, ISession caller);

    /**
     * @param name
     * @param caller
     * @return
     */
    IRole findRole(String name, ISession caller);

    /**
     * @param roleId
     * @param caller
     * @return
     */
    IRole getRole(String roleId, ISession caller);

    /**
     * @param caller
     * @return
     */
    List<? extends IRole> getRoles(ISession caller);

    /**
     * @param name
     * @param caller
     * @return
     */
    IUser createUser(String name, ISession caller);

    /**
     * @param user
     * @param password
     * @param caller
     */
    void changePassword(IUser user, byte[] password, ISession caller);

    /**
     * @param user
     * @param caller
     */
    void activateUser(IUser user, ISession caller);

    /**
     * @param user
     * @param caller
     */
    void deactivateUser(IUser user, ISession caller);

    /**
     * @param user
     * @param caller
     */
    void deleteUser(IUser user, ISession caller);

    /**
     * @param name
     * @param caller
     * @return
     */
    IUser findUser(String name, ISession caller);

    /**
     * @param userId
     * @param caller
     * @return
     */
    IUser getUser(String userId, ISession caller);

    /**
     * @param caller
     * @return
     */
    List<? extends IUser> getUsers(ISession caller);

    /**
     * @param user
     * @param role
     * @param caller
     */
    void grantRole(IUser user, IRole role, ISession caller);

    /**
     * @param user
     * @param role
     * @param caller
     */
    void revokeRole(IUser user, IRole role, ISession caller);

    /**
     * @param user
     * @param permission
     * @param objectURI
     * @param caller
     */
    void grantPermission(IUser user, IPermission permission, String objectURI, ISession caller);

    /**
     * @param user
     * @param permissions
     * @param objectURI
     * @param caller
     */
    void grantPermissions(IUser user, Set<IPermission> permissions, String objectURI, ISession caller);

    /**
     * @param role
     * @param permission
     * @param objectURI
     * @param caller
     */
    void grantPermission(IRole role, IPermission permission, String objectURI, ISession caller);

    /**
     * @param role
     * @param permissions
     * @param objectURI
     * @param caller
     */
    void grantPermissions(IRole role, Set<IPermission> permissions, String objectURI, ISession caller);

    /**
     * @param user
     * @param permission
     * @param objectURI
     * @param caller
     */
    void revokePermission(IUser user, IPermission permission, String objectURI, ISession caller);

    /**
     * @param user
     * @param permissions
     * @param objectURI
     * @param caller
     */
    void revokePermissions(IUser user, Set<IPermission> permissions, String objectURI, ISession caller);

    /**
     * @param role
     * @param permission
     * @param objectURI
     * @param caller
     */
    void revokePermission(IRole role, IPermission permission, String objectURI, ISession caller);

    /**
     * @param role
     * @param permissions
     * @param objectURI
     * @param caller
     */
    void revokePermissions(IRole role, Set<IPermission> permissions, String objectURI, ISession caller);

    /**
     * @param caller
     * @param permission
     * @param objectURI
     * @return
     */
    boolean hasPermission(ISession caller, IPermission permission, String objectURI);

	void addUserManagementListener(IUserManagementListener tenantView);
}
