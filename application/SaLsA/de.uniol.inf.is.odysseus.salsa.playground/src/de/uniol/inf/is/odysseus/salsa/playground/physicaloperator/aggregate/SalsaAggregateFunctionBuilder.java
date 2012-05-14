package de.uniol.inf.is.odysseus.salsa.playground.physicaloperator.aggregate;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.salsa.playground.physicaloperator.aggregate.functions.RelationalPolygonAggregation;

public class SalsaAggregateFunctionBuilder implements IAggregateFunctionBuilder {

	private static Collection<String> names = new LinkedList<String>();
	{
		names.add("L1Merge");
	}
	
	@Override
    public IAggregateFunction<Tuple<?>, Tuple<?>> createAggFunction(
			AggregateFunction key, int[] pos) {
		
		IAggregateFunction<Tuple<?>, Tuple<?>> aggFunc = null;
		
		if(key.getName().equalsIgnoreCase("L1Merge")) {
			aggFunc = new RelationalPolygonAggregation<Tuple<?>, Tuple<?>>(pos);
		}  
		
		else {
			throw new IllegalArgumentException("No such Aggregatefunction");
		}
		
		return aggFunc;
	}

	@Override
	public String getDatamodel() {
		return "relational";
	}

	@Override
	public Collection<String> getFunctionNames() {
		return names;
	}

}
