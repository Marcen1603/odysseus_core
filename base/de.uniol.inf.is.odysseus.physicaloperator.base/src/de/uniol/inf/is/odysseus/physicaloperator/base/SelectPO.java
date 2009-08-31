package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;

/**
 * @author Jonas Jacobi
 */
public class SelectPO<T> extends AbstractPipe<T, T> {

	private IPredicate<? super T> predicate;

	public SelectPO(IPredicate<? super T> predicate) {
		this.predicate = predicate.clone();
	}

	@Override
	public boolean modifiesInput() {
		return false;
	}
	
	@Override
	protected synchronized void process_next(T object, int port) {
//		System.out.println("SelectPO (" + this.hashCode() + "): " + object);
		if (predicate.evaluate(object)) {
			transfer(object);
		}
	}

	@Override
	public SelectPO<T> clone() {
		SelectPO<T> spo = (SelectPO<T>) super.clone();
		spo.predicate = this.predicate;
		return spo;
	}
	
	@Override
	public void process_open() throws OpenFailedException{
		super.process_open();
		this.predicate.init();
	}

}
