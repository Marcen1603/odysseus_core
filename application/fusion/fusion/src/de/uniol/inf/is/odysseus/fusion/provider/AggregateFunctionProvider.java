package de.uniol.inf.is.odysseus.fusion.provider;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.relational.base.Tuple;
import de.uniol.inf.is.odysseus.fusion.aggregate.function.spatialfusion.RelationalPolygonAggregation;
import de.uniol.inf.is.odysseus.fusion.aggregate.function.tracking.TrackingAggregation;

public class AggregateFunctionProvider implements IAggregateFunctionBuilder {

	private static Collection<String> names = new LinkedList<String>();
	{
		names.add("L1Fusion");
		names.add("Tracker");
	}
	
	@Override
    public IAggregateFunction<Tuple<?>, Tuple<?>> createAggFunction(AggregateFunction key, int[] pos) {
		
		IAggregateFunction<Tuple<?>, Tuple<?>> aggFunc = null;
		
		if(key.getName().equalsIgnoreCase("L1Fusion")) {
			aggFunc = new RelationalPolygonAggregation<Tuple<?>, Tuple<?>>(pos);
		}  
		if(key.getName().equalsIgnoreCase("Tracker")) {
			aggFunc = new TrackingAggregation<Tuple<?>, Tuple<?>>(pos);
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
