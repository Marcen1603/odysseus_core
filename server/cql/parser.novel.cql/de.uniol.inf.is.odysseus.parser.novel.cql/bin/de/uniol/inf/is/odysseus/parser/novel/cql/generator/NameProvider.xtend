package de.uniol.inf.is.odysseus.parser.novel.cql.generator

import de.uniol.inf.is.odysseus.core.mep.IFunction
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry
import de.uniol.inf.is.odysseus.mep.FunctionStore
import de.uniol.inf.is.odysseus.mep.MEP

class NameProvider 
{

	public def boolean isAggregation(String name)
	{
		//TODO .toUpperCase
		println("AGGREGATEPATTERN::" + AggregateFunctionBuilderRegistry.aggregatePattern)
		return AggregateFunctionBuilderRegistry.aggregatePattern.matcher(name).toString.contains(name)
	}
	
	public def boolean isMapper(String name, String function)
	{
		if(FunctionStore.instance.containsSymbol(name))
		{
			try
			{
				var datatype = MEP.instance.parse(function).returnType
				for(IFunction<?> f : FunctionStore.instance.getFunctions(name))
					if(f.returnType.equals(datatype))
						return true
			} 
			catch(Exception e) 
			{
				return false
			}
		}
		return false	
	}
}