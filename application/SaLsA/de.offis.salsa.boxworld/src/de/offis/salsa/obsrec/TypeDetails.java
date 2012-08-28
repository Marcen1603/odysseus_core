package de.offis.salsa.obsrec;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import de.offis.salsa.obsrec.TrackedObject.Type;

public class TypeDetails implements Iterable<Type> {
	private HashMap<Type, Double> types = new HashMap<Type, Double>();
			
	public TypeDetails(Type type, Double affinity) {
		this.types.put(type, affinity);
	}
	
	public TypeDetails() {
		
	}
	
	public void addTypeAffinity(Type type, Double affinity){
		this.types.put(type, affinity);
	}
	
	public Double getTypeAffinity(Type type){
		if(!this.types.containsKey(type)){
			return 0.0;
		} else {
			return this.types.get(type);
		}
	}
	
	public Type getMaxAffinityType(){
		Double affnity = -1.0;
		Type type = null;
		
		for (Entry<Type, Double> t : types.entrySet()) {
			if (type == null || t.getValue() > affnity) {
				type = t.getKey();
				affnity = t.getValue();
			}
		}
		
		return type;
	}
	
	@Override
	public String toString() {
		return types.toString();
	}

	@Override
	public Iterator<Type> iterator() {
		return types.keySet().iterator();
	}
}