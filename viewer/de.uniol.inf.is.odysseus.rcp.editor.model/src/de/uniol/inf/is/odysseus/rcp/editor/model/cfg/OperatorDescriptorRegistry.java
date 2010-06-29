package de.uniol.inf.is.odysseus.rcp.editor.model.cfg;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OperatorDescriptorRegistry {

	private static OperatorDescriptorRegistry instance = null;
	
	private final Map<String, IOperatorDescriptor> opDescs = new HashMap<String, IOperatorDescriptor>();
	
	private OperatorDescriptorRegistry() {
		
	}
	
	public static OperatorDescriptorRegistry getInstance() {
		if( instance == null ) 
			instance = new OperatorDescriptorRegistry();
		return instance;
	}
	
	public void add( IOperatorDescriptor opDesc ) {
		if( opDesc == null ) return;
		opDescs.put(opDesc.getName(), opDesc);
	}
	
	public void add( IOperatorDescriptorProvider provider ) {
		Collection<IOperatorDescriptor> desc = provider.getOperatorDescriptors();
		for( IOperatorDescriptor d : desc ) 
			add(d);
	}
	
	public IOperatorDescriptor get( String name ) {
		return opDescs.get(name);
	}
	
	public void remove( String name ) {
		opDescs.remove(name);
	}
	
	public void remove( IOperatorDescriptor desc ) {
		remove(desc.getName());
	}
	
	public Collection<IOperatorDescriptor> getAll() {
		return opDescs.values();
	}
	
	public Collection<String> getNames() {
		return opDescs.keySet();
	}
}
