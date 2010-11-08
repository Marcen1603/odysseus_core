package de.uniol.inf.is.odysseus.scars.util;

import java.util.ArrayList;
import java.util.List;

public class StreamCollector {

	private List< List<Object>> recievedObjects = new ArrayList<List<Object>>();
	
	public StreamCollector( int numOfPorts ) {
		for( int i = 0; i < numOfPorts; i++ )
			recievedObjects.add(new ArrayList<Object>());
	}
	
	public void recieve( Object punctuationOrTuple, int port ) {
		recievedObjects.get(port).add(punctuationOrTuple);
	}
	
	public boolean isReady() {
		for( int i = 0; i < getPortCount(); i++){
			if( recievedObjects.get(i).isEmpty())
				return false;
		}
		return true;
	}
	
	public List<Object> getNext() {
		List<Object> list = new ArrayList<Object>();
		if( !isReady() ) 
			return list; // leere Liste
		
		for( int i = 0; i < getPortCount(); i++ ) {
			list.add(recievedObjects.get(i).get(0));
			recievedObjects.get(i).remove(0);
		}
		
		return list;
	}
	
	public int getPortCount() {
		return recievedObjects.size();
	}
}
