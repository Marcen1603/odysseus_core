package de.offis.client.iconsgrid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class ListDataProvider<T> extends DataProvider<T>{

	private ArrayList<T> dataList = new ArrayList<T>();
	
	@Override
	public ArrayList<T> getData() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addAll(Collection<? extends T> items){
		dataList.addAll(items);
		applySort();
		
		// find and write new indezies in array
		HashMap<Integer, T> addedItems = new HashMap<Integer, T>();
		for(T item : items){
			addedItems.put(dataList.indexOf(item), item);
		}		
		
		fireChangeEvent(new DataChangeEvent<T>(addedItems));
	}
	
	@Override
	public void add(T item) {
		dataList.add(item);
		applySort();
		fireChangeEvent(new DataChangeEvent<T>(item, dataList.indexOf(item)));
	}

	@Override
	public void remove(T item) {
		if(dataList.contains(item)){
			dataList.remove(item);
			fireChangeEvent(new DataChangeEvent<T>(DataChangeEvent.REMOVE, item));
		}
	}

	@Override
	public void forceUpdate() {
		fireChangeEvent(new DataChangeEvent<T>(DataChangeEvent.FORCE_UPDATE, null));
	}

	@Override
	public void clear() {
		dataList.clear();
		fireChangeEvent(new DataChangeEvent<T>(DataChangeEvent.CLEAR, null));
	}

	@Override
	protected void applySort() {
		if(this.comparator != null){
			Collections.sort(dataList, this.comparator);
		}
	}
}
