package de.uniol.inf.is.odysseus.kdds.frequent.physical;

import java.util.List;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class SpaceSavingFrequentItem<T extends RelationalTuple<?>> extends AbstractFrequentPO<T> {

	public SpaceSavingFrequentItem(int[] onAttributes, double size) {
		super(onAttributes, size);
	}

	public SpaceSavingFrequentItem(SpaceSavingFrequentItem<T> other) {
		this(other.getOnAttributes(), other.getSize());		
	}

	@Override
	protected void process_next(T newObject, int port) {
		RelationalTuple<?> tuple = newObject.restrict(getOnAttributes(), true);				
		if (this.items.containsKey(tuple)) {
			int oldCount = this.items.get(tuple);
			this.items.put(tuple, oldCount + 1);
		} else {
			if (this.items.size() < (getSize())) {
				this.items.put(tuple, 1);
			} else {
				synchronized (items) {			
					RelationalTuple<?> smallest = null;
					int smallestValue = Integer.MAX_VALUE;
					for (Entry<RelationalTuple<?>, Integer> e : items.entrySet()) {						
						if(e.getValue()<smallestValue){
							smallest = e.getKey();
							smallestValue = e.getValue();
						}
					}					
					this.items.remove(smallest);
					this.items.put(newObject, smallestValue+1);								
				}
			}
		}
		List<T> liste = listtoobjects(items);
		transfer(liste);	
		
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new SpaceSavingFrequentItem<T>(this);
	}

	

}
