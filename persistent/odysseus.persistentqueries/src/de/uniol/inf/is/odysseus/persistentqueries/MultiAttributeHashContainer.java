package de.uniol.inf.is.odysseus.persistentqueries;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class MultiAttributeHashContainer {

	/**
	 * The relational tuple to store
	 * in a hash map.
	 */
	private RelationalTuple tuple;
	
	private RelationalTuple restrictedTuple;
	
	/**
	 * if the hashCode has been calculated once
	 * do not calculate it anymore;
	 */
	int hashCode;
	
	public MultiAttributeHashContainer(RelationalTuple tuple, RelationalTuple restrictedTuple){
		this.tuple = tuple;
		this.restrictedTuple = restrictedTuple;
		this.hashCode = this.restrictedTuple.hashCode();
	}
	
	public int hashCode(){
		return this.hashCode;
	}
	
	public boolean equals(MultiAttributeHashContainer other){
		return this.restrictedTuple.equals(other.restrictedTuple);
	}
	
	public RelationalTuple getTuple(){
		return this.tuple;
	}
	
	public RelationalTuple getRestrictedTuple(){
		return this.restrictedTuple;
	}
}
