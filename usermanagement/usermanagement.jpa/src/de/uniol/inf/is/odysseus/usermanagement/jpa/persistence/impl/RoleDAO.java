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
package de.uniol.inf.is.odysseus.usermanagement.jpa.persistence.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;

import de.uniol.inf.is.odysseus.usermanagement.jpa.domain.impl.RoleImpl;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class RoleDAO extends GenericDAOImpl<RoleImpl, String> {

    public RoleDAO() {
        super(RoleImpl.class);
    }

    /**
     * @param type
     */
    public RoleDAO(final Class<RoleImpl> type) {
        super(type);
    }

    @Override
    public RoleImpl findByName(final String name) {
        return this.getSingleResult(this.startNamedQuery(RoleImpl.NQ_FIND_BY_NAME).add("name", name));
    }

    @PersistenceUnit(unitName = "odysseusPU")
    public void setEntityManager(final EntityManager em) {
        this.entityManager = em;
    }
}
