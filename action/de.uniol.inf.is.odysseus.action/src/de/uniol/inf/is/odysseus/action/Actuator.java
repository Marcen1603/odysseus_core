package de.uniol.inf.is.odysseus.action;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.action.actuatorManagement.ActuatorFactory;

public class Actuator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		ActuatorFactory factory = ActuatorFactory.getInstance();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
