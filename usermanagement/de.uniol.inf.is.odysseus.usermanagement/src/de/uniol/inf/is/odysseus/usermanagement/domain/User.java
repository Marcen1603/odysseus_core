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
package de.uniol.inf.is.odysseus.usermanagement.domain;

import java.security.Principal;
import java.util.List;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *         TODO Move to base bundle
 */
public interface User extends AbstractEntity, Principal, Comparable<User> {
    /**
     * @return The name of this user.
     */
    @Override
    String getName();

    /**
     * @return Whether the user account is active or not
     */
    boolean isActive();

    /**
     * @return The roles of the user
     */
    List<? extends Role> getRoles();

    /**
     * @return The privileges of the user
     */
    List<? extends Privilege> getPrivileges();

    /**
     * Validates if the given password is correct for this user.
     * 
     * @param password
     * @return
     */
    boolean validatePassword(byte[] password);

}
