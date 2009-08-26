package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.PartialAggregate;


class ElementPartialAggregate<T extends Comparable> implements PartialAggregate<T> {
	T elem;
	
	public ElementPartialAggregate(T elem) {
		setElem(elem);
	}
	
	public ElementPartialAggregate(PartialAggregate<T> p) {
		setElem(((ElementPartialAggregate<T>)p).getElem());
	}

	public T getElem() {
		return elem;
	}
	
	public void setElem(T elem) {
		this.elem = elem;
	}

}
