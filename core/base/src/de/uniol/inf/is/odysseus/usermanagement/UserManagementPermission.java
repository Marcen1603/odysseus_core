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

import de.uniol.inf.is.odysseus.usermanagement.IPermission;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public enum UserManagementPermission implements IPermission {
    CREATE_USER, ALTER_USER, DELETE_USER, DEACTIVATE_USER, SET_SYSTEM_USER,

    CREATE_ROLE, DELETE_ROLE,

    GRANT, GRANT_ALL, REVOKE, REVOKE_ALL, GRANT_ROLE, REVOKE_ROLE,

    LOGOUT, GET_ALL_USER, GET_ALL;
}
