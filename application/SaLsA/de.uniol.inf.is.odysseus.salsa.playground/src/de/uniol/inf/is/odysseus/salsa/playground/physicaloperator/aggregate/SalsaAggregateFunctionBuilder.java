package de.uniol.inf.is.odysseus.salsa.playground.physicaloperator.aggregate;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.salsa.playground.physicaloperator.aggregate.functions.RelationalPolygonAggregation;

public class SalsaAggregateFunctionBuilder implements IAggregateFunctionBuilder {

	private static Collection<String> names = new LinkedList<String>();
	{
		names.add("L1Merge");
	}
	
	@Override
    public IAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> createAggFunction(
			AggregateFunction key, int[] pos) {
		
		IAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> aggFunc = null;
		
		if(key.getName().equalsIgnoreCase("L1Merge")) {
			aggFunc = new RelationalPolygonAggregation<RelationalTuple<?>, RelationalTuple<?>>(pos);
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
