package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IPartialAggregate;


public class ElementPartialAggregate<T> implements IPartialAggregate<T> {
	T elem;
	
	public ElementPartialAggregate(T elem) {
		setElem(elem);
	}
	
	public ElementPartialAggregate(IPartialAggregate<T> p) {
		setElem(((ElementPartialAggregate<T>)p).getElem());
	}

	public T getElem() {
		return elem;
	}
	
	public void setElem(T elem) {
		this.elem = elem;
	}

	@Override
	public String toString() {
		return ""+elem;
	}
	
	@Override
	public ElementPartialAggregate clone(){
		return new ElementPartialAggregate(this);
	}
	
}
