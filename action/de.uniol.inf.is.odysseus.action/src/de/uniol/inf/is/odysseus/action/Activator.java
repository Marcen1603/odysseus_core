package de.uniol.inf.is.odysseus.action;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import de.uniol.inf.is.odysseus.action.actuatorManagement.ActuatorFactory;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuatorManager;

public class Activator implements BundleActivator {

	private IActuatorManager service;
	private ActuatorFactory factory;

	@Override
	public void start(BundleContext context) throws Exception {
		ActuatorFactory factory = ActuatorFactory.getInstance();
		ServiceReference reference = context.getServiceReference(IActuatorManager.class.getName());
		this.service = (IActuatorManager) context.getService(reference);
		if (this.service != null){
			factory.bindActuatorManager(service);
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		this.factory = null;
	}

}
