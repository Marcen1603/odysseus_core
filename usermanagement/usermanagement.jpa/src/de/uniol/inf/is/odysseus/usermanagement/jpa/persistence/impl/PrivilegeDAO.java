/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;

import de.uniol.inf.is.odysseus.usermanagement.jpa.domain.impl.PrivilegeImpl;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class PrivilegeDAO extends GenericDAOImpl<PrivilegeImpl, String> {
    public PrivilegeDAO() {
        super(PrivilegeImpl.class);
    }

    /**
     * @param type
     */
    public PrivilegeDAO(final Class<PrivilegeImpl> type) {
        super(type);
    }

    public List<PrivilegeImpl> findByObjectURI(final String objectURI) {
        return this.getResultList(this.startNamedQuery(PrivilegeImpl.NQ_FIND_BY_OBJECTURI).add("objectURI", objectURI));
    }

    @Override
    public PrivilegeImpl findByName(String name) {
    	throw new IllegalArgumentException("NO NAME IN PRIVILEGE!");
    }
    
    @PersistenceUnit(unitName = "odysseusPU")
    public void setEntityManager(final EntityManager em) {
        this.entityManager = em;
    }
}
