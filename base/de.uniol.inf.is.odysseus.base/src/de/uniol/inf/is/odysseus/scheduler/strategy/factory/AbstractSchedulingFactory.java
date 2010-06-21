package de.uniol.inf.is.odysseus.scheduler.strategy.factory;

import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;

/**
 * Base class for scheduling strategy factories. Sets the name of the factory
 * based on OSGi properties.
 * 
 * @author Wolf Bauer
 * 
 */
public abstract class AbstractSchedulingFactory implements
		ISchedulingFactory {
	/**
	 * Name for this factory. Should be unique.
	 */
	private String name = null;

	/**
	 * OSGi-Method: Is called when this object will be activated by OSGi (after
	 * constructor and bind-methods). This method can be used to configure this
	 * object.
	 * 
	 * @param context
	 *            OSGi {@link ComponentContext} provides informations about the
	 *            OSGi environment.
	 */
	protected void activate(ComponentContext context) {
		setName(context.getProperties());
	}

	/**
	 * Set the name of this factory by properties.
	 * 
	 * @param properties
	 *            Properties containing "component.readableName" or
	 *            "component.name" as name for this factory.
	 */
	protected void setName(Dictionary<?, ?> properties) {
		name = ((String) properties.get("component.readableName"));
		if (name == null) {
			name = ((String) properties.get("component.name"));
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

}
