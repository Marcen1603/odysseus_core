package de.uniol.inf.is.odysseus.jmx.services;

import javax.management.MBeanServer;
import javax.management.ObjectName;

public interface JMXRegistryService {

void addManagedBean(OdysseusManagedBean bean);
void removeManagedBean(OdysseusManagedBean bean);

MBeanServer getMBeanServer();
}
