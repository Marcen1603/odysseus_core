package de.uniol.inf.is.odysseus.physicaloperator.aggregate;

import java.util.Collection;


import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IAggregateFunction;

public interface IAggregateFunctionBuilder {

	String getDatamodel();
	Collection<String> getFunctionNames();
	IAggregateFunction<?,?> createAggFunction(AggregateFunction e2, int[] posArray);

}
