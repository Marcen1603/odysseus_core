package de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter.MultithreadedOperatorSettings;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

public class ReplicationTransformationStrategy extends AbstractMultithreadedTransformationStrategy<AggregateAO>{

	@Override
	public String getName() {
		return "ReplicationTransformationStrategy";
	}

	@Override
	public List<Class<? extends AbstractFragmentAO>> getAllowedFragmentationTypes() {
		// this strategy needs no fragmentation
		return new ArrayList<Class<? extends AbstractFragmentAO>>();
	}

	@Override
	public Class<? extends AbstractFragmentAO> getPreferredFragmentationType() {
		// this strategy needs no fragmentation
		return null;
	}

	@Override
	public int evaluateCompatibility(ILogicalOperator operator) {
		return 0;
	}

	@Override
	public boolean transform(ILogicalOperator operator,
			MultithreadedOperatorSettings settingsForOperator) {
		if (!super.areSettingsValid(settingsForOperator)){
			return false;
		}
		
		
		
		return true;
	}

	@Override
	protected void doStrategySpecificPostParallelization(
			ILogicalOperator parallelizedOperator,
			ILogicalOperator currentExistingOperator,
			ILogicalOperator currentClonedOperator, int iteration, List<AbstractFragmentAO> fragments) {
		// no strategy specific modifications
	}

}
