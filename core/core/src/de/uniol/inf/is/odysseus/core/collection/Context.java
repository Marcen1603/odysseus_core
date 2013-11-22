package de.uniol.inf.is.odysseus.core.collection;

import java.io.Serializable;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

public class Context {

	private final Map<String, Serializable> contextMap = Maps.newHashMap();
		
	// for readability
	public static Context empty() {
		return new Context();
	}
	
	public void put(String key, Serializable value){
		if (containsKey(key)){
			throw new IllegalArgumentException("Key "+key+" already used. Delete first.");
		}
		putImpl(key, value);
	}
	
	public void putOrReplace( String key, Serializable value ) {
		putImpl(key, value);
	}

	private Serializable putImpl(String key, Serializable value) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "Key for context must not be null or empty!");
		Preconditions.checkNotNull(value, "Serializable value must not be null for key %s!", key);
		
		return contextMap.put(key, value);
	}
	
	public Serializable get(String key){
		return contextMap.get(key);
	}
	
	public final boolean containsKey( String key ) {
		return contextMap.containsKey(key);
	}
	
	public final boolean containsValue( Serializable value ) {
		return contextMap.containsValue(value);
	}
	
	public final ImmutableCollection<String> getKeys() {
		return ImmutableSet.copyOf(contextMap.keySet());
	}
	
	public final ImmutableCollection<Serializable> getValues() {
		return ImmutableList.copyOf(contextMap.values());
	}
	
	public final void remove( String key ) {
		contextMap.remove(key);
	}
}
