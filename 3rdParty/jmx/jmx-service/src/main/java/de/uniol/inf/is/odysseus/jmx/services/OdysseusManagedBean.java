package de.uniol.inf.is.odysseus.jmx.services;

/**
 * A Managed Bean interface
 *
 * @author Christian Kuka
 * @version 1.0
 */
public interface OdysseusManagedBean {

    /**
     *
     * @return The MBean
     */
    Object getMBean();

    /**
     *
     * @return The object name
     */
    String getObjectName();

    /**
     * 
     * @return The alias for this object
     */
    String getObjectNameAlias();
}
