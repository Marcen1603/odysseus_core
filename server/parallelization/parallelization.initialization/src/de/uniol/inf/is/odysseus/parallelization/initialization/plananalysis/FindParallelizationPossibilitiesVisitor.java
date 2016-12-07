/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.plananalysis;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.IParallelizableOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.IParallelizationIndividualConfiguration;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.interoperator.InterOperatorParallelizationConfiguration;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.intraoperator.IntraOperatorParallelizationConfiguration;

/**
 * @author Dennis Nowak
 *
 */
public class FindParallelizationPossibilitiesVisitor<P extends ILogicalOperator>
		implements IGraphNodeVisitor<P, List<IParallelizationIndividualConfiguration>> {

	private static Logger LOG = LoggerFactory.getLogger(FindParallelizationPossibilitiesVisitor.class);

	public static final String UNIQUE_ID_PREFIX = ".pid.";

	private static int counter = 0;

	private List<IParallelizationIndividualConfiguration> possibleParallelizations = new ArrayList<>();
	private String queryIdentifier;

	public FindParallelizationPossibilitiesVisitor(String queryIdentifier) {
		this.queryIdentifier = queryIdentifier;
	}

	@Override
	public void nodeAction(P node) {
		// set unique identifier
		if (node.getUniqueIdentifier() == null || node.getUniqueIdentifier().length() == 0) {
			if (!(node instanceof StreamAO)) {
				String identifier = new String(UNIQUE_ID_PREFIX);
				identifier += this.queryIdentifier;
				identifier += ".";
				identifier += counter++;
				identifier += UNIQUE_ID_PREFIX;
				node.setUniqueIdentifier(identifier);
			}
		}
		if (isIntraParallizable(node)) {
			LOG.debug("Node " + node.getUniqueIdentifier() + " is intraoperator parallizable.");
			IParallelizationIndividualConfiguration config = new IntraOperatorParallelizationConfiguration(node);
			possibleParallelizations.add(config);
		}
		if (node instanceof IParallelizableOperator) {
			switch (node.getStateType()) {
			case UNKNOWN:
				LOG.debug("Node " + node.getUniqueIdentifier() + " has unknown state type.");
				break;
			case STATELESS:
				IParallelizationIndividualConfiguration statelessConfig = new InterOperatorParallelizationConfiguration(
						node, "GenericStatelessTransformationStrategy", "RoundRobinFragmentAO");
				this.possibleParallelizations.add(statelessConfig);
				LOG.debug("Node " + node.getUniqueIdentifier() + " is stateless.");
				break;
			case PARTITIONED_STATE:
				IParallelizationIndividualConfiguration partitionConfig = new InterOperatorParallelizationConfiguration(
						node, "GenericGroupedTransformationStrategy", "HashFragmentAO");
				this.possibleParallelizations.add(partitionConfig);
				LOG.debug("Node " + node.getUniqueIdentifier() + " has partitioned state.");
				break;
			case ARBITRARY_STATE:
				LOG.debug("Node " + node.getUniqueIdentifier() + " has arbitrary state.");
				break;
			default:
				LOG.info("State type can not be handled by automatic parallelization");
			}
		} else {
			LOG.debug("Node " + node.getUniqueIdentifier() + " is not interaoperator parallizable.");
		}
	}

	@Override
	public void beforeFromSinkToSourceAction(P sink, P source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterFromSinkToSourceAction(P sink, P source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeFromSourceToSinkAction(P source, P sink) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterFromSourceToSinkAction(P source, P sink) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<IParallelizationIndividualConfiguration> getResult() {
		return this.possibleParallelizations;
	}

	private boolean isIntraParallizable(ILogicalOperator operator) {
		// FIXME ask Operator if it is parallelizable with current configuration
		// TODO make it configurable
		if (operator instanceof AggregateAO) {
			return true;
		}
		return false;
	}

}
