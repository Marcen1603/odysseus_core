package de.uniol.inf.is.odysseus.base;

public interface IDataDictionaryListener {

	public void addedViewDefinition( DataDictionary sender, String name, ILogicalOperator op );
	public void removedViewDefinition( DataDictionary sender, String name, ILogicalOperator op );
	
}
