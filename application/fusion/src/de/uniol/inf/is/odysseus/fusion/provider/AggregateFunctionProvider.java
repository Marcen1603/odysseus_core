package de.uniol.inf.is.odysseus.fusion.provider;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.fusion.level1.aggregate.function.RelationalPolygonAggregation;
import de.uniol.inf.is.odysseus.fusion.tracking.aggregate.function.TrackingAggregation;
import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class AggregateFunctionProvider implements IAggregateFunctionBuilder {

	private static Collection<String> names = new LinkedList<String>();
	{
		names.add("L1Fusion");
		names.add("Tracker");
	}
	
	public IAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> createAggFunction(AggregateFunction key, int[] pos) {
		
		IAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> aggFunc = null;
		
		if(key.getName().equalsIgnoreCase("L1Fusion")) {
			aggFunc = new RelationalPolygonAggregation<RelationalTuple<?>, RelationalTuple<?>>(pos);
		}  
		if(key.getName().equalsIgnoreCase("Tracker")) {
			aggFunc = new TrackingAggregation<RelationalTuple<?>, RelationalTuple<?>>(pos);
		}  
		
		
		if(aggFunc == null) {
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
