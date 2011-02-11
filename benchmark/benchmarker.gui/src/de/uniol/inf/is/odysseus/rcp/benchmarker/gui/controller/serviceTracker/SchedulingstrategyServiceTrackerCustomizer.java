package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.serviceTracker;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;

/**
 * Diese Klasse holt sich die Schedulingstrategienservices aus Odysseus
 * 
 * @author Stefanie Witzke
 * 
 */
public class SchedulingstrategyServiceTrackerCustomizer implements ServiceTrackerCustomizer {

	private final BundleContext context;
	private final List<String> schedulingstrategyNames;

	public SchedulingstrategyServiceTrackerCustomizer(BundleContext context, final List<String> schedulingstrategyNames) {
		this.context = context;
		this.schedulingstrategyNames = schedulingstrategyNames;
	}

	@Override
	public Object addingService(ServiceReference reference) {
		ISchedulingFactory service = (ISchedulingFactory) context.getService(reference);
		schedulingstrategyNames.add(reference.getProperty("component.ReadableName").toString());
		return service;
	}

	@Override
	public void modifiedService(ServiceReference reference, Object service) {
		removedService(reference, service);
		addingService(reference);
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		schedulingstrategyNames.remove(reference.getProperty("component.ReadableName").toString());
		context.ungetService(reference);
	}
}
