package de.uniol.inf.is.odysseus.kdds.frequent.physical;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class SimpleFrequentItemPO<T extends RelationalTuple<?>> extends AbstractFrequentPO<T> {

	private static Logger logger = LoggerFactory.getLogger(SimpleFrequentItemPO.class);
	
	public SimpleFrequentItemPO(int[] restrictList, double size) {
		super(restrictList, size);
		if((size%1)!=0){
			logger.warn("Item size \""+size+"\" for a simple frequent item counter should be an integer!");
		}		
	}
	
	public SimpleFrequentItemPO(SimpleFrequentItemPO<?> other){
		this(other.getOnAttributes(), other.getSize());		
	}	
	
	@Override
	protected void process_next(T newObject, int port) {		
		RelationalTuple<?> tuple = newObject.restrict(getOnAttributes(), true);				
		if (this.items.containsKey(tuple)) {
			int oldCount = this.items.get(tuple);
			this.items.put(tuple, oldCount + 1);
		} else {
			if (this.items.size() < (getSize() - 1)) {
				this.items.put(tuple, 1);
			} else {
				synchronized (items) {
					@SuppressWarnings("unchecked")
					Map<T, Integer> walker = (Map<T, Integer>) this.items.clone();
					
					for (Entry<T, Integer> e : walker.entrySet()) {
						if (e.getValue() == 1) {
							this.items.remove(e.getKey());
						} else {							
							this.items.put(e.getKey(), e.getValue() - 1);
						}
					}				
				}
			}
		}
		List<T> liste = listtoobjects(items);
		transfer(liste);		
	}
	


	@Override
	public SimpleFrequentItemPO<T> clone() {
		return new SimpleFrequentItemPO<T>(this);
	}

}
