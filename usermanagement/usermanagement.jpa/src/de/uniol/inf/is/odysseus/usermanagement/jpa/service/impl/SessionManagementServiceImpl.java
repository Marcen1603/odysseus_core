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
package de.uniol.inf.is.odysseus.usermanagement.jpa.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.usermanagement.AbstractSessionManagement;
import de.uniol.inf.is.odysseus.usermanagement.ISessionManagement;
import de.uniol.inf.is.odysseus.usermanagement.jpa.domain.impl.UserImpl;
import de.uniol.inf.is.odysseus.usermanagement.jpa.persistence.impl.UserDAO;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class SessionManagementServiceImpl extends AbstractSessionManagement<UserImpl> implements ISessionManagement {

	private EntityManagerFactory entityManagerFactory;

	protected void activate(ComponentContext context) {
		this.entityManagerFactory = Persistence.createEntityManagerFactory("odysseusPU");
		final EntityManager em = this.entityManagerFactory.createEntityManager();
		
		userDAO = new UserDAO();
		
		((UserDAO)userDAO).setEntityManager(em);
	}

}
