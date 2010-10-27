package de.uniol.inf.is.odysseus.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

/**
 * @author Marco Grawunder
 */

public class SplitPO<T> extends AbstractPipe<T, T> {

	private List<IPredicate<? super T>> predicates;

	public SplitPO(List<IPredicate<? super T>> predicates)  {
		super();
		initPredicates(predicates);
	}

	public SplitPO(SplitPO<T> splitPO) {
		super();
		initPredicates(splitPO.predicates);
	}
	
	private void initPredicates(List<IPredicate<? super T>> predicates) {
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
	
	@Override
	public SplitPO<T> clone() {
		return new SplitPO<T>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof SplitPO)) {
			return false;
		}
		SplitPO spo = (SplitPO) ipo;
		if(this.getSubscribedToSource().equals(spo.getSubscribedToSource()) &&
				this.predicates.size() == spo.predicates.size()) {
			for(int i = 0; i<this.predicates.size(); i++) {
				if(!this.predicates.get(i).equals(spo.predicates.get(i))) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

}
