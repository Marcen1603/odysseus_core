package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IPartialAggregate;


class ElementPartialAggregate<T extends Comparable<T>> implements IPartialAggregate<T> {
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

}
