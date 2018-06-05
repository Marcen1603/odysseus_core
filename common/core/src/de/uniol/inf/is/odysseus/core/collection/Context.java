package de.uniol.inf.is.odysseus.core.collection;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.sun.xml.internal.bind.AnyTypeAdapter;

@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public class Context implements Serializable {

	private static final long serialVersionUID = 5200675232639616018L;
	
	private final Map<String, Serializable> contextMap = Maps.newHashMap();
		
	// for readability
	public static Context empty() {
		return new Context();
	}
	
	public void put(String key, Serializable value){		
		if (containsKey(key.toUpperCase())){
			throw new IllegalArgumentException("Key "+key+" already used. Delete first.");
		}
		putImpl(key.toUpperCase(), value);
	}
	
	public void putOrReplace( String key, Serializable value ) {
		putImpl(key.toUpperCase(), value);
	}

	private Serializable putImpl(String key, Serializable value) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "Key for context must not be null or empty!");
		Preconditions.checkNotNull(value, "Serializable value must not be null for key %s!", key);
		
		return contextMap.put(key, value);
	}
	
	public Serializable get(String key){
		return contextMap.get(key.toUpperCase());
	}
	
	public final boolean containsKey( String key ) {
		return contextMap.containsKey(key.toUpperCase());
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
		contextMap.remove(key.toUpperCase());
	}

	public Context copy() {
		Context copy = new Context();
		for( String key : contextMap.keySet() ) {
			Serializable o = contextMap.get(key);
			copy.contextMap.put(key, o);
		}
		return copy;
	}
}
