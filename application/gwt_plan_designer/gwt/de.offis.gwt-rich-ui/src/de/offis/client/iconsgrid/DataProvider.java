package de.offis.client.iconsgrid;

import java.util.ArrayList;
import java.util.Comparator;

public abstract class DataProvider<T> {
	// TODO implement pagination?
	private ArrayList<UsesDataProvider<T>> handlerList = new ArrayList<UsesDataProvider<T>>();
	protected Comparator<T> comparator = null;
	
	
	public void registerChangeHandler(UsesDataProvider<T> handler) {
		this.handlerList.add(handler);
	}
	
	public void removeChangeHandler(UsesDataProvider<T> handler) {
		this.handlerList.remove(handler);
	}
	
	public void fireChangeEvent(DataChangeEvent<T> event){
		for(UsesDataProvider<T> handler : handlerList){
			handler.onDataProviderAction(event);
		}
	}
	
	
	public abstract ArrayList<T> getData();
	public abstract void add(T item);
	public abstract void remove(T item);
	public abstract void forceUpdate();
	public abstract void clear();
	protected abstract void applySort();
	
	public void setDataSorter(Comparator<T> comparator){
		this.comparator = comparator;
	}
}
