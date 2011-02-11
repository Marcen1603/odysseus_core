package de.uniol.inf.is.odysseus.datamining;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.datamining.classification.builder.ClassifyAOBuilder;
import de.uniol.inf.is.odysseus.datamining.classification.builder.HoeffdingTreeAOBuilder;
import de.uniol.inf.is.odysseus.datamining.clustering.builder.LeaderAOBuilder;
import de.uniol.inf.is.odysseus.datamining.clustering.builder.SimpleSinglePassKMeansAOBuilder;
import de.uniol.inf.is.odysseus.datamining.state.builder.RecallAOBuilder;
import de.uniol.inf.is.odysseus.datamining.state.builder.StateAOBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;

public class Activator implements BundleActivator {

	private static final String LEADER = "LEADER";
	private static final String HOEFFDINGTREE = "HOEFFDINGTREE";
	private static final String CLASSIFY = "CLASSIFY";
	private static final String SIMPLESINGLEPASSKMEANS = "SIMPLESINGLEPASSKMEANS";
	
	private static final String STATE = "STATE";
	private static final String RECALL = "RECALL";
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
		OperatorBuilderFactory.putOperatorBuilderType(CLASSIFY, ClassifyAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(SIMPLESINGLEPASSKMEANS, SimpleSinglePassKMeansAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(STATE, StateAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(RECALL, RecallAOBuilder.class);
		
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
		OperatorBuilderFactory.removeOperatorBuilderType(CLASSIFY);
		OperatorBuilderFactory.removeOperatorBuilderType(SIMPLESINGLEPASSKMEANS);
		OperatorBuilderFactory.removeOperatorBuilderType(STATE);
		OperatorBuilderFactory.removeOperatorBuilderType(RECALL);
	}


}
