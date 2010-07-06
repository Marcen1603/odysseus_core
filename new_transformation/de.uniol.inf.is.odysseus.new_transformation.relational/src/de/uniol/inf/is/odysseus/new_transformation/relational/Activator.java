package de.uniol.inf.is.odysseus.new_transformation.relational;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.MapAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.new_transformation.relational.transformators.AtomicDataInputStreamAccessPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.relational.transformators.ByteBufferReceiverPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.relational.transformators.FixedSetPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.relational.transformators.InputStreamAccessPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.relational.transformators.MapAOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.relational.transformators.ProjectAOTransformator;
import de.uniol.inf.is.odysseus.transformation.greedy.TransformationDatabase;

public class Activator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		TransformationDatabase db = TransformationDatabase.getInstance();

		// AccesAO
		db.registerTransformator(AccessAO.class, new AtomicDataInputStreamAccessPOTransformator());
		db.registerTransformator(AccessAO.class, new ByteBufferReceiverPOTransformator());
		db.registerTransformator(AccessAO.class, new FixedSetPOTransformator());
		db.registerTransformator(AccessAO.class, new InputStreamAccessPOTransformator());
		
		db.registerTransformator(ProjectAO.class, new ProjectAOTransformator());
		
		db.registerTransformator(MapAO.class, new MapAOTransformator());
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
