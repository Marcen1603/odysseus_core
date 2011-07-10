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

import de.uniol.inf.is.odysseus.usermanagement.domain.Session;
import de.uniol.inf.is.odysseus.usermanagement.domain.User;
import de.uniol.inf.is.odysseus.usermanagement.domain.impl.SessionImpl;
import de.uniol.inf.is.odysseus.usermanagement.persistence.impl.UserDAO;
import de.uniol.inf.is.odysseus.usermanagement.policy.LogoutPolicy;
import de.uniol.inf.is.odysseus.usermanagement.service.SessionmanagementService;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class SessionmanagementServiceImpl implements SessionmanagementService {

    private final UserDAO userDAO = new UserDAO();
    private final SessionStore sessionStore = SessionStore.getInstance();

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.SessionmanagementService
     * #login(java.lang.String, byte[])
     */
    @Override
    public Session login(final String username, final byte[] password) {
        final User user = this.userDAO.findByName(username);
        if (user.isActive() && user.validatePassword(password)) {
            if (this.sessionStore.containsKey(user.getId())) {
                this.sessionStore.remove(user.getId());
            }
            final SessionImpl session = new SessionImpl(user);
            this.sessionStore.put(session.getId(), session);
            return session;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.SessionmanagementService
     * #logout(de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public void logout(final Session caller) {
        final SessionStore sessionStore = SessionStore.getInstance();
        final Session session = sessionStore.get(caller.getId());
        if (LogoutPolicy.allow(session.getUser(), caller.getUser())) {
            sessionStore.remove(session.getId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.service.SessionmanagementService
     * #isValid(de.uniol.inf.is.odysseus.usermanagement.domain.Session,
     * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
     */
    @Override
    public boolean isValid(final Session session, final Session caller) {
        if (session.getUser() != null) {
            final Session realSession = this.sessionStore.get(session.getId());
            this.sessionStore.get(caller.getId());
            if (realSession.isValid()) {
                return true;
            }
        }
        return false;
    }

}
