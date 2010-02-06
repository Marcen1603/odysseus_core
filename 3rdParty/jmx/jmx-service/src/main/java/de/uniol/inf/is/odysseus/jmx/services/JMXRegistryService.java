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
