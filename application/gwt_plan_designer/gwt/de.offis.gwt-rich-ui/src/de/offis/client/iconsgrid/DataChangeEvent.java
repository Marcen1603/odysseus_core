package de.offis.client.iconsgrid;

import java.util.HashMap;

public class DataChangeEvent<T> {
	public static final int ADD = 111;
	public static final int REMOVE = 222;
	public static final int FORCE_UPDATE = 333;
	public static final int ADD_ALL = 444;
	public static final int CLEAR = 555;
	
	private int type;
	private T changedObject;
	private HashMap<Integer, T> items;
	private int listIndex = -1;
	
	public DataChangeEvent(int type, T changedObject) {
		switch(type){
		case ADD:
		case ADD_ALL:
			// TODO throw exception or so to indicate this is the wrong constructor
			break;
		}
		
		this.type = type;
		this.changedObject = changedObject;
	}
	
	/**
	 * Constructor for an ADD event.
	 * @param changedObject
	 * The added object
	 * @param listIndex
	 * The index where the DataProivder stored the object (sorting)
	 */
	public DataChangeEvent(T changedObject, int listIndex) {
		this.type = ADD;
		this.changedObject = changedObject;
		this.listIndex = listIndex;
	}
	
	/**
	 * Constructor for an ADD_ALL event.
	 * @param items
	 * @param itemIndezies
	 */
	public DataChangeEvent(HashMap<Integer, T> items) {
		this.type = ADD_ALL;
		this.items = items;
	}

	public int getType() {
		return type;
	}
	
	public T getChangedObject() {
		return changedObject;
	}
	
	public HashMap<Integer, T> getItems() {
		return items;
	}
	
	public int getListIndex() {
		return listIndex;
	}
}
