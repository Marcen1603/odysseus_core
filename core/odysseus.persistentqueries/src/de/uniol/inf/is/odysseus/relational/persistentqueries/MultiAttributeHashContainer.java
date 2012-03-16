package de.uniol.inf.is.odysseus.relational.persistentqueries;

import de.uniol.inf.is.odysseus.relational.base.Tuple;

@SuppressWarnings({"rawtypes"})
public class MultiAttributeHashContainer {

	/**
	 * The relational tuple to store
	 * in a hash map.
	 */
	private Tuple tuple;
	
	private Tuple restrictedTuple;
	
	/**
	 * if the hashCode has been calculated once
	 * do not calculate it anymore;
	 */
	int hashCode;
	
	public MultiAttributeHashContainer(Tuple tuple, Tuple restrictedTuple){
		this.tuple = tuple;
		this.restrictedTuple = restrictedTuple;
		this.hashCode = this.restrictedTuple.hashCode();
	}
	
	@Override
    public int hashCode(){
		return this.hashCode;
	}
	
	public boolean equals(MultiAttributeHashContainer other){
		return this.restrictedTuple.equals(other.restrictedTuple);
	}
	
	public Tuple getTuple(){
		return this.tuple;
	}
	
	public Tuple getRestrictedTuple(){
		return this.restrictedTuple;
	}
}
