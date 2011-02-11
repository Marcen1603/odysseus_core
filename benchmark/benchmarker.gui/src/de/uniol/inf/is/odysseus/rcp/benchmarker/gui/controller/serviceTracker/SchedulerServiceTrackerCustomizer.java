package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.serviceTracker;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import de.uniol.inf.is.odysseus.scheduler.ISchedulerFactory;

/**
 * Diese Klasse holt sich die Schedulingservices aus Odysseus
 * 
 * @author Stefanie Witzke
 * 
 */
public class SchedulerServiceTrackerCustomizer implements ServiceTrackerCustomizer {

	private final BundleContext context;
	private final List<String> schedulerNames;

	public SchedulerServiceTrackerCustomizer(BundleContext context, final List<String> schedulerNames) {
		this.context = context;
		this.schedulerNames = schedulerNames;
	}

	@Override
	public Object addingService(ServiceReference reference) {
		ISchedulerFactory service = (ISchedulerFactory) context.getService(reference);
		schedulerNames.add(reference.getProperty("component.ReadableName").toString());
		return service;
	}

	@Override
	public void modifiedService(ServiceReference reference, Object service) {
		removedService(reference, service);
		addingService(reference);
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		schedulerNames.remove(reference.getProperty("component.ReadableName").toString());
		context.ungetService(reference);
	}
}
