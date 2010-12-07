package de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class ListPartialAggregate<T> implements IPartialAggregate<T> {
	
	final List<T> elems;
	
	public ListPartialAggregate(T elem) {
		elems = new LinkedList<T>();
		addElem(elem);
	}
	
	public ListPartialAggregate(ListPartialAggregate<T> p) {
		this.elems = new LinkedList<T>(p.elems);
	}

	public List<T> getElems() {
		return elems;
	}
	
	public ListPartialAggregate<T> addElem(T elem) {
		this.elems.add(elem);
		return this;
	}

	@Override
	public String toString() {
		return ""+elems;
	}
	
	@Override
	public ElementPartialAggregate clone(){
		return new ElementPartialAggregate(this);
	}
	
}
