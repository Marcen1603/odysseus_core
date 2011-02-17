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
package de.uniol.inf.is.odysseus.jmx.services;

import javax.management.MBeanServer;

/**
 * The JMX Registry where bundles can register themself
 * 
 * @author Christian Kuka
 * @version 1.0
 */
public interface JMXRegistryService {

    /**
     * Add an OdysseusManagedBean to the JMX service
     * @param bean The MBean to manage
     */
    void addManagedBean(OdysseusManagedBean bean);

    /**
     * Remove an OdysseusManagedBean to the JMX service
     * @param bean The MBean to remove
     */
    void removeManagedBean(OdysseusManagedBean bean);

    /**
     * 
     * @return The MBean server on this platform
     */
    MBeanServer getMBeanServer();
}
