package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;
import de.uniol.inf.is.odysseus.scheduler.AbstractSchedulerFactory;

/**
 * Factory for creating {@link SingleThreadSchedulerWithStrategy} instances.
 * 
 * @author Wolf Bauer
 * 
 */
public class SingleThreadSchedulerFactoryRR extends AbstractSchedulerFactory {

	/**
	 * OSGi-Method: Is called when this object will be activated by OSGi (after
	 * constructor and bind-methods). This method can be used to configure this
	 * object.
	 * 
	 * @param context
	 *            OSGi {@link ComponentContext} provides informations about the
	 *            OSGi environment.
	 */
	@Override
	protected void activate(ComponentContext context) {
		super.activate(context);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.scheduler.ISchedulerFactory#createScheduler(de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory)
	 */
	@Override
	public IScheduler createScheduler(
			ISchedulingFactory schedulingStrategy) {
		return new SingleThreadSchedulerWithStrategy(schedulingStrategy);
	}

}
