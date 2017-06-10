package de.uniol.inf.is.odysseus.parser.novel.cql.generator


@Deprecated
class NameProvider 
{

	public def boolean isAggregation(String name)
	{
		
		
		//TODO .toUpperCase
//		return AggregateFunctionBuilderRegistry.aggregatePattern.matcher(name).toString.contains(name)
		return false; 
	}
	
	public def boolean isMapper(String name, String function)
	{
//		if(FunctionStore.instance.containsSymbol(name))
//		{
//			try
//			{
//				var datatype = MEP.instance.parse(function).returnType
//				for(IFunction<?> f : FunctionStore.instance.getFunctions(name))
//					if(f.returnType.equals(datatype))
//						return true
//			} 
//			catch(Exception e) 
//			{
//				return false
//			}
//		}
		return false	
	}
}