package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class SelectPO<T> extends AbstractPipe<T, T> {

	private IPredicate<? super T> predicate;

	public IPredicate<? super T> getPredicate() {
		return predicate;
	}

	public SelectPO(IPredicate<? super T> predicate) {
		this.predicate = predicate.clone();
	}
	
	public SelectPO(SelectPO<T> po){
		this.predicate = po.predicate.clone();		
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	@Override
	protected synchronized void process_next(T object, int port) {
		if (predicate.evaluate(object)) {
			transfer(object);
		}
	}
	
	@Override
	public void process_open() throws OpenFailedException{
		super.process_open();
		this.predicate.init();
	}
	
	@Override
	public SelectPO<T> clone() throws CloneNotSupportedException {
		return new SelectPO<T>(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((predicate == null) ? 0 : predicate.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SelectPO<?> other = (SelectPO<?>) obj;
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		return true;
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp) {
		sendPunctuation(timestamp);
	}		

}
