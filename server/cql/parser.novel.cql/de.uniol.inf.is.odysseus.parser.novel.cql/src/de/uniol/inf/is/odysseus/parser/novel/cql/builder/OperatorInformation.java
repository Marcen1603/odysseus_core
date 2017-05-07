package de.uniol.inf.is.odysseus.parser.novel.cql.builder;

import java.util.Map;

import de.uniol.inf.is.odysseus.parser.novel.cql.builder.PQLBuilder.Type;

public class OperatorInformation 
{

	String name;
	String[] arguments;
	Map<String, Type> argumentTypes;
	
	public OperatorInformation(String name, String[] arguments, Type[] types) 
	{
		this.name = name;
		this.arguments = arguments;
		for(int i = 0; i < arguments.length; i++)
			argumentTypes.put(arguments[i], types[i]);
	}
	
}
