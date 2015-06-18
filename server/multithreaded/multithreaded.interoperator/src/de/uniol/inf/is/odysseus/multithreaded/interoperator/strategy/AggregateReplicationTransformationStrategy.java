package de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter.MultithreadedOperatorSettings;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

public class AggregateReplicationTransformationStrategy extends AbstractMultithreadedTransformationStrategy<AggregateAO>{

	@Override
	public String getName() {
		return "AggregateReplicationTransformationStrategy";
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

}
