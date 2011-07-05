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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.uniol.inf.is.odysseus.usermanagement.domain.impl.UserImpl;
import de.uniol.inf.is.odysseus.usermanagement.persistence.impl.UserDAO;
import de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService;
import de.uniol.inf.is.odysseus.usermanagement.service.impl.UsermanagementServiceImpl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class Activator implements BundleActivator {

    private static BundleContext context;

    static BundleContext getContext() {
        return context;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    public void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;

        // FIXME Test Code---------
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("odysseusPU");
        UserDAO userDAO = new UserDAO();
        EntityManager em = emf.createEntityManager();
        userDAO.setEntityManager(emf.createEntityManager());
        UserImpl user = new UserImpl();
        userDAO.create(user);
        // ------------------
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }

}
