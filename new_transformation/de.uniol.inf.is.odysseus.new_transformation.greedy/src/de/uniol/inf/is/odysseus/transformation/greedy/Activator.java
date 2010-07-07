package de.uniol.inf.is.odysseus.transformation.greedy;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.AggregateAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.AggregateAOTransformator;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.JoinTIPOTransformator;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.SelectAOTransformator;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.accessAO.AccessAOViewTransformator;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.accessAO.ExistingAccessAO;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO.RelationalElementWindowAOTransformator;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO.SlidingAdvanceTimeWindowTIPOTransformator;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO.SlidingElementWindowTIPOTransformator;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO.SlidingPeriodicWindowTIPOTransformator;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO.SlidingTimeWindowTIPOTransformator;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO.UnboundedWindowTransformator;

public class Activator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		try {
			TransformationDatabase db = TransformationDatabase.getInstance();

			// AccesAO
			db.registerTransformator(AccessAO.class,
					new AccessAOViewTransformator());
			db.registerTransformator(AccessAO.class, new ExistingAccessAO());

			// windowAO
			db.registerTransformator(WindowAO.class,
					new SlidingAdvanceTimeWindowTIPOTransformator());
			db.registerTransformator(WindowAO.class,
					new SlidingElementWindowTIPOTransformator());
			db.registerTransformator(WindowAO.class,
					new SlidingPeriodicWindowTIPOTransformator());
			db.registerTransformator(WindowAO.class,
					new SlidingTimeWindowTIPOTransformator());
			db.registerTransformator(WindowAO.class,
					new RelationalElementWindowAOTransformator());
			db.registerTransformator(WindowAO.class,
					new UnboundedWindowTransformator());

			db.registerTransformator(SelectAO.class,
					new SelectAOTransformator());
			db.registerTransformator(JoinAO.class, new JoinTIPOTransformator());
			db.registerTransformator(AggregateAO.class,
					new AggregateAOTransformator());

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}

}
