package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.serviceTracker;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;

/**
 * Diese Klasse holt sich die Bufferplacementstrategieservices aus Odysseus
 * 
 * @author Stefanie Witzke
 * 
 */
public class BufferplacementstrategyServiceTrackerCustomizer implements ServiceTrackerCustomizer {

	private final BundleContext context;
	private final List<String> bufferplacementstrategyNames;

	public BufferplacementstrategyServiceTrackerCustomizer(BundleContext context,
			final List<String> bufferplacementstrategyNames) {
		this.context = context;
		this.bufferplacementstrategyNames = bufferplacementstrategyNames;
	}

	@Override
	public Object addingService(ServiceReference reference) {
		IBufferPlacementStrategy service = (IBufferPlacementStrategy) context.getService(reference);
		bufferplacementstrategyNames.add(service.getName());
		return service;
	}

	@Override
	public void modifiedService(ServiceReference reference, Object service) {
		removedService(reference, service);
		addingService(reference);
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		bufferplacementstrategyNames.remove(reference.getProperty("component.Name").toString());
		context.ungetService(reference);
	}

}
