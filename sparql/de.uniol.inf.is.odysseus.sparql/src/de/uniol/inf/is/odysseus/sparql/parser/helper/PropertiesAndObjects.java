package de.uniol.inf.is.odysseus.sparql.parser.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class PropertiesAndObjects {
	
	private HashMap<INode, List<INode>> props;
	
	public PropertiesAndObjects(){
		this.props = new HashMap<INode, List<INode>>();
	}
	
	public Set<INode> getProperties(){
		return this.props.keySet();
	}
	
	public List<INode> getObjects(INode property){
		return this.props.get(property);
	}
	
	public void putPropertyAndObjects(INode property, List<INode> objects){
		this.props.put(property, objects);
	}

}
