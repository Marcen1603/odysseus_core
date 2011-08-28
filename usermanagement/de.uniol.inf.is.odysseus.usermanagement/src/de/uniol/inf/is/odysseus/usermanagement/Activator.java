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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class Activator implements BundleActivator {

    private static BundleContext context;

    static BundleContext getContext() {
        return Activator.context;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void start(final BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;

        // FIXME Test Code---------
        // final EntityManagerFactory emf =
        // Persistence.createEntityManagerFactory("odysseusPU");
        // final UserDAO userDAO = new UserDAO();
        // final EntityManager em = emf.createEntityManager();
        // userDAO.setEntityManager(emf.createEntityManager());
        // final UserImpl user = new UserImpl();
        // userDAO.create(user);
        //
        // final PrivilegeDAO privilegeDAO = new PrivilegeDAO();
        // privilegeDAO.setEntityManager(em);
        // final PrivilegeImpl privilege = new PrivilegeImpl();
        // privilege.addPermission(UsermanagementPermission.ALTER_USER);
        // privilegeDAO.create(privilege);
        // userDAO.findAll();
        // final List<PrivilegeImpl> privileges = privilegeDAO.findAll();
        // for (final PrivilegeImpl priv : privileges) {
        // for (final Permission per : priv.getPermissions()) {
        // System.out.println(per.toString());
        // }
        // }
        // ------------------
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }

}
