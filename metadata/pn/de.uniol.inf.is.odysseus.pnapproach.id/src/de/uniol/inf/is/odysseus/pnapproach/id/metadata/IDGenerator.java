package de.uniol.inf.is.odysseus.pnapproach.id.metadata;

import java.util.ArrayList;
import java.util.List;

public class IDGenerator {

	private static long ID = 1;
	
	/**
	 * this variable will be used, if a wildcard is necessary for an id
	 * we will use the object reference and not the value
	 */
	private static Long wildcard = new Long(-1);
	
	private static long genID(){
		return ++ID;
	}
	
	public static List<Long> nextID(){
		ArrayList<Long> idList = new ArrayList<Long>();
		idList.add(new Long(genID()));
		return idList;
	}
	
	public static Long getWildcard(){
		return wildcard;
	}
}
