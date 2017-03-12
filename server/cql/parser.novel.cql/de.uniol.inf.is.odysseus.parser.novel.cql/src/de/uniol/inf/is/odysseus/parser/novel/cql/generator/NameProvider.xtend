package de.uniol.inf.is.odysseus.parser.novel.cql.generator

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry
import de.uniol.inf.is.odysseus.mep.MEP
import de.uniol.inf.is.odysseus.mep.FunctionSignature

class NameProvider 
{

	public def boolean isAggregation(String name)
	{
		return AggregateFunctionBuilderRegistry.aggregatePattern.matcher(name).find()
	}
	
	public def boolean isMapper(String name)
	{
		for(FunctionSignature sig : MEP.functions)
			if(sig.symbol.equals(name.toUpperCase))		
				return true
		return false
	}
	
}