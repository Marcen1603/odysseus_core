package de.uniol.inf.is.odysseus.datamining;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.datamining.classification.builder.HoeffdingTreeAOBuilder;
import de.uniol.inf.is.odysseus.datamining.clustering.builder.LeaderAOBuilder;
import de.uniol.inf.is.odysseus.datamining.clustering.builder.SimpleSinglePassKMeansAOBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;

public class Activator implements BundleActivator {

	private static final String LEADER = "LEADER";
	private static final String HOEFFDINGTREE = "HOEFFDINGTREE";
	private static final String SIMPLESINGLEPASSKMEANS = "SIMPLESINGLEPASSKMEANS";
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		OperatorBuilderFactory.putOperatorBuilderType(LEADER, LeaderAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(HOEFFDINGTREE, HoeffdingTreeAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(SIMPLESINGLEPASSKMEANS, SimpleSinglePassKMeansAOBuilder.class);
		
        }
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {		
		OperatorBuilderFactory.removeOperatorBuilderType(LEADER);
		OperatorBuilderFactory.removeOperatorBuilderType(HOEFFDINGTREE);
		OperatorBuilderFactory.removeOperatorBuilderType(SIMPLESINGLEPASSKMEANS);
	}


}
