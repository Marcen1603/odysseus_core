package de.uniol.inf.is.odysseus.parser.novel.cql.tests.util

import de.uniol.inf.is.odysseus.parser.novel.cql.generator.NameProvider

class NameProviderHelper extends NameProvider
{

	override isAggregation(String name)
	{
		var list = newArrayList
		list.add('COUNT')
		list.add('MAX')
		list.add('MIN')
		list.add('SUM')
		list.add('AVG')
		return list.contains(name)
	}
	
	override isMapper(String name) 
	{
		var list = newArrayList
		list.add('DolToEur')
		return list.contains(name)
	}
	
}	