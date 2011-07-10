/**
 * Copyright 2010 Melsa Project
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
package de.uniol.inf.is.odysseus.usermanagement.domain.impl;

import java.util.UUID;

import de.uniol.inf.is.odysseus.usermanagement.domain.Session;
import de.uniol.inf.is.odysseus.usermanagement.domain.User;

/**
 *
 */
public class SessionImpl implements Session {
    private final static long SESSION_TIMEOUT = 10 * 60000;
    private final String id = UUID.randomUUID().toString();
    private final User user;
    private final long start;
    private long end;

    public SessionImpl(final User user) {
        this.user = user;
        start = System.currentTimeMillis();
        end = start + SESSION_TIMEOUT;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uniol.inf.is.odysseus.usermanagement.domain.Session#getId()
     */
    @Override
    public String getId() {
        return this.id;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uniol.inf.is.odysseus.usermanagement.domain.Session#getUser()
     */
    @Override
    public User getUser() {
        return this.user;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uniol.inf.is.odysseus.usermanagement.domain.Session#isValid()
     */
    @Override
    public boolean isValid() {
        long timestamp = System.currentTimeMillis();
        return timestamp >= start && timestamp <= end;
    }

    public void updateSession() {
        if (isValid()) {
            this.end = System.currentTimeMillis()+SESSION_TIMEOUT;
        }
    }
}
