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
package de.uniol.inf.is.odysseus.jmx.impl;

import de.uniol.inf.is.odysseus.jmx.services.JMXRegistryService;
import de.uniol.inf.is.odysseus.jmx.services.OdysseusManagedBean;

// Apache Felix SCR
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;

// JMX Stuff
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

import java.util.concurrent.ConcurrentHashMap;
import org.apache.felix.scr.annotations.Component;

/**
 * The implementation of the JMX Registry Service
 * @author Christian Kuka
 * @version 1.0
 */
@Component(immediate = true)
@Service(value = JMXRegistryService.class)
@Reference(name = "odysseusManagedBean", referenceInterface = OdysseusManagedBean.class, bind = "addManagedBean", unbind = "removeManagedBean", cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class JMXRegistryServiceImpl implements JMXRegistryService {

    /**
     * The MBean Server
     */
    private final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
    /**
     * A map with all registered MBeans
     */
    private final ConcurrentHashMap<String, ObjectName> map = new ConcurrentHashMap<String, ObjectName>();

    @Override
    public void addManagedBean(OdysseusManagedBean bean) {
        if (bean.getMBean() == null) {
            return;
        }
        try {
            ObjectName name = new ObjectName(bean.getObjectName());
            server.registerMBean(bean.getMBean(), name);
            map.put(bean.getObjectNameAlias(), name);
        } catch (Exception ex) {
            //TODO: Replace this with a logger
            ex.printStackTrace();
        }
    }

    @Override
    public void removeManagedBean(OdysseusManagedBean bean) {
        map.remove(bean.getObjectNameAlias());
    }

    @Override
    public MBeanServer getMBeanServer() {
        return server;
    }

    public ObjectName getObjectName(String alias) {
        return map.get(alias);
    }
}
