package de.uniol.inf.is.odysseus.metadata.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.base.FESortedPair;
import de.uniol.inf.is.odysseus.base.IClone;

public class PairMap<K1 extends Comparable<K1>,K2 extends Comparable<K2>,V,M extends IClone> extends MetaAttributeContainer<M>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 719541540005684180L;
	private Map<FESortedPair<K1,K2>,V> content = null;
	
	public PairMap() {
		content = new HashMap<FESortedPair<K1,K2>, V>();
	}
	
	public PairMap(PairMap<K1, K2, V, M> name) {
		content = new HashMap<FESortedPair<K1,K2>, V>(name.content);
	}

	public void put(K1 k1, K2 k2, V value){
		FESortedPair<K1, K2> p = new FESortedPair<K1, K2>(k1,k2);
		put(p, value);
	}
	
	public void put(FESortedPair<K1, K2> p, V value){
		content.put(p, value);
	}
	
	public V get(K1 k1, K2 k2){
		FESortedPair<K1, K2> p = new FESortedPair<K1,K2>(k1,k2);
		return get(p);
	}
	
	public V get(FESortedPair<K1, K2> p){
		return content.get(p);
	}
	
	public Set<Entry<FESortedPair<K1, K2>, V>> entrySet(){
		return content.entrySet();
	}
	
	@Override
	public String toString() {
		return ""+content;
	}
	
	@Override
	public PairMap<K1,K2,V,M> clone() {
		return new PairMap<K1, K2, V, M>(this);
	}

	
}