package de.uniol.inf.is.odysseus.core.server.datadictionary;

public interface IDatadictionaryProviderListener {
	void newDatadictionary(IDataDictionary dd);
	void removedDatadictionary(IDataDictionary dd);
}
