package de.offis.client.iconsgrid;

public interface SelectionHandler<T> {
	void onItemSelected(T item);
	void onItemDeselected(T item);
}
