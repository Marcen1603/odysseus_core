package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;

/**
 * @author Marco Grawunder
 */

public class SplitPO<T> extends AbstractPipe<T, T> {

	private List<IPredicate<? super T>> predicates;

	public SplitPO(List<IPredicate<? super T>> predicates) {
		super();
		this.predicates = new ArrayList<IPredicate<? super T>>(predicates.size());
		for (IPredicate<? super T> p: predicates){
			this.predicates.add(p.clone());
		}
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public void process_open() throws OpenFailedException{
		super.process_open();
		for (IPredicate<? super T> p: predicates){
			p.init();
		}		
	}
	
	@Override
	protected void process_next(T object, int port) {
		for (int i=0;i<predicates.size();i++){
			if (predicates.get(i).evaluate(object)) {
				transfer(object,i);
				return;
			}
		}
		transfer(object,predicates.size());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((predicates == null) ? 0 : predicates.hashCode());
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
		SplitPO<?> other = (SplitPO<?>) obj;
		if (predicates == null) {
			if (other.predicates != null)
				return false;
		} else if (!predicates.equals(other.predicates))
			return false;
		return true;
	}
	
	

}
