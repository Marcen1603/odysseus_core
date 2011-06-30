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

import de.uniol.inf.is.odysseus.usermanagement.domain.User;
import de.uniol.inf.is.odysseus.usermanagement.persistence.impl.UserDAO;
import de.uniol.inf.is.odysseus.usermanagement.service.SessionmanagementService;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class SessionmanagementServiceImpl implements SessionmanagementService {

    private final UserDAO userDAO = new UserDAO();

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.SessionmanagementService
     * #login(java.lang.String, byte[])
     */
    @Override
    public void login(String username, byte[] password) {
        User user = userDAO.findByName(username);
        if (user.validatePassword(password)) {

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.SessionmanagementService
     * #logout(de.uniol.inf.is.odysseus.usermanagement.domain.User)
     */
    @Override
    public void logout(User caller) {

    }

}
