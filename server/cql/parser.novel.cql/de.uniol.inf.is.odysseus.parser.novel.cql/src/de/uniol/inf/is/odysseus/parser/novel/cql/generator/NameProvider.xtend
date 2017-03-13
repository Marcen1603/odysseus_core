package de.uniol.inf.is.odysseus.parser.novel.cql.generator

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry
import de.uniol.inf.is.odysseus.mep.FunctionStore

class NameProvider 
{

	public def boolean isAggregation(String name)
	{
		return AggregateFunctionBuilderRegistry.aggregatePattern.matcher(name).toString.contains(name)
	}
	
	public def boolean isMapper(String name)
	{
		/*
		 * MEP functions and aggregation functions may share the the same name and can therefore only be 
		 * distinguished by their signature. The signature cannot be provided by a cql statement. A MEP 
		 * function is only recognized as such if it does not share its name with an aggregation function.
		 */
		return FunctionStore.instance.containsSymbol(name) && !isAggregation(name)
	}
}