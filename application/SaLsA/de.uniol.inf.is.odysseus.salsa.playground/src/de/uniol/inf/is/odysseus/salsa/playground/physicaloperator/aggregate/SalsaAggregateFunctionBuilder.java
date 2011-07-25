package de.uniol.inf.is.odysseus.salsa.playground.physicaloperator.aggregate;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.relational.AggregationBean;
import de.uniol.inf.is.odysseus.physicaloperator.relational.AggregationJSR223;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalAvgSum;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalCount;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMinMax;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalNest;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalStdDev;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.salsa.playground.physicaloperator.aggregate.functions.RelationalPolygonAggregation;

public class SalsaAggregateFunctionBuilder implements IAggregateFunctionBuilder {

	private static Collection<String> names = new LinkedList<String>();
	{
		names.add("PMERGE");
//		names.add("AVG");
//		names.add("SUM");
//		names.add("COUNT");
//		names.add("MIN");
//		names.add("MAX");
//		names.add("NEST");
//		names.add("STDDEV");
//		names.add("BEAN");
//		names.add("SCRIPT");
	}
	
	public IAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> createAggFunction(
			AggregateFunction key, int[] pos) {
		
		IAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> aggFunc = null;
		
		if(key.getName().equalsIgnoreCase("PMERGE")) {
			aggFunc = new RelationalPolygonAggregation(pos);
		} 
//		else if(key.getName().equalsIgnoreCase("Dummy_2")){
//			
//		}
		
//		else if (key.getName().equalsIgnoreCase("COUNT")) {
//			aggFunc = RelationalCount.getInstance();
//		} else if ((key.getName().equalsIgnoreCase("MIN"))
//				|| (key.getName().equalsIgnoreCase("MAX"))) {
//			aggFunc = RelationalMinMax.getInstance(pos[0],
//					(key.getName().equalsIgnoreCase("MAX")) ? true : false);
//		}else if ((key.getName().equalsIgnoreCase("STDDEV"))){
//			aggFunc = new RelationalStdDev(pos);
//		} else if ((key.getName().equalsIgnoreCase("NEST"))) {
//			aggFunc = new RelationalNest(pos);
//		} else if (key.getName().equalsIgnoreCase("BEAN")) {
//			aggFunc = new AggregationBean(pos, key.getProperty("resource"));
//		} else if (key.getName().equalsIgnoreCase("SCRIPT")) {
//			aggFunc = new AggregationJSR223(pos, key.getProperty("resource"));
		
		
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
