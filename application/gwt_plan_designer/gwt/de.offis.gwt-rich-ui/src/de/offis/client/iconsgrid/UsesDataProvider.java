package de.offis.client.iconsgrid;

public interface UsesDataProvider<T> {
	void onDataProviderAction(DataChangeEvent<T> event);
}
