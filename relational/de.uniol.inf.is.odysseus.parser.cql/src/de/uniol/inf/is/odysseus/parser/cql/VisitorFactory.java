package de.uniol.inf.is.odysseus.parser.cql;

import java.util.HashMap;
import java.util.Map;

public class VisitorFactory {

	private Map<String, IVisitor> visitorMap = null;
	
	static VisitorFactory instance = null; 
	
	private VisitorFactory(){
		visitorMap = new HashMap<String, IVisitor>();
	}
	
	public static synchronized VisitorFactory getInstance(){
		if (instance == null){
			instance = new VisitorFactory();
		}
		return instance;
	}
	
	public synchronized boolean setVisitor(IVisitor visitor, String name){
		visitorMap.put(name, visitor);
		return true;
	}
	
	public synchronized IVisitor getVisitor(String name){
		return visitorMap.get(name);
	}
	
	
}
