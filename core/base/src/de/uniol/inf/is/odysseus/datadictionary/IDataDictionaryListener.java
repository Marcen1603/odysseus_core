package de.uniol.inf.is.odysseus.datadictionary;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;

public interface IDataDictionaryListener {

	public void addedViewDefinition( DataDictionary sender, String name, ILogicalOperator op );
	public void removedViewDefinition( DataDictionary sender, String name, ILogicalOperator op );
	
}
