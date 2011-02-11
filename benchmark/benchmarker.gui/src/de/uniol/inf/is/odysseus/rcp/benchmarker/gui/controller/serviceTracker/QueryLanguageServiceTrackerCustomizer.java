package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.serviceTracker;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import de.uniol.inf.is.odysseus.planmanagement.IQueryParser;

/**
 * Diese Klasse holt sich die QueryLanguageServices aus Odysseus
 * 
 * @author Stefanie Witzke
 * 
 */
public class QueryLanguageServiceTrackerCustomizer implements ServiceTrackerCustomizer {

	private final BundleContext context;
	private final List<String> queryLanguageNames;

	public QueryLanguageServiceTrackerCustomizer(BundleContext context, final List<String> queryLanguageNames) {
		this.context = context;
		this.queryLanguageNames = queryLanguageNames;
	}

	@Override
	public Object addingService(ServiceReference reference) {
		IQueryParser service = (IQueryParser) context.getService(reference);
		queryLanguageNames.add(service.getLanguage());
		return service;
	}

	@Override
	public void modifiedService(ServiceReference reference, Object service) {
		removedService(reference, service);
		addingService(reference);
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		queryLanguageNames.remove(reference.getProperty("component.Name").toString());
		context.ungetService(reference);
	}
}
