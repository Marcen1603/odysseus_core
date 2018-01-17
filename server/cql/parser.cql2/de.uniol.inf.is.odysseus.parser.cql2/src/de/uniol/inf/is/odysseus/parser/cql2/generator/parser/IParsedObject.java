package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

public interface IParsedObject {

	public enum Type {
		EXPRESSION,
		AGGREGATION,
		ATTRIBUTE,
	}
	
	Type getType();
	
	String getName();
	String getDataType();
	String getAlias();
	
}
