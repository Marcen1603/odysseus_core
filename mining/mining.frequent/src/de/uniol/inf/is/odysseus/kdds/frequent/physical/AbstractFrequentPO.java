package de.uniol.inf.is.odysseus.kdds.frequent.physical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public abstract class AbstractFrequentPO<T extends RelationalTuple<?>> extends AbstractPipe<T,T>{

	private double size = 0;
	private int[] onAttributes;
	
	public AbstractFrequentPO(int[] onAttributes, double size){
		this.size = size;
		this.onAttributes = onAttributes;
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp, port);		
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	public double getSize(){
		return this.size;
	}
	
	
	public int[] getOnAttributes(){
		return this.onAttributes;
	}
	
	public boolean isEqualOnAttributes(T left, T right){
		RelationalTuple<?> leftTuple = left.restrict(onAttributes, true);
		RelationalTuple<?> rightTuple = right.restrict(onAttributes, true);
		return leftTuple.equals(rightTuple);		
	}
	
	@SuppressWarnings("unchecked")
	protected List<T> listtoobjects(Map<RelationalTuple<?>, Integer> countedItems) {
		List<T> list = new ArrayList<T>();
		for(Entry<RelationalTuple<?>, Integer> e : countedItems.entrySet()){
			RelationalTuple<?> newOne = e.getKey().append(e.getValue());
			list.add((T)newOne);
		}		
		return list;
	}
	
	// Object T and its counter as an Integer
	protected HashMap<RelationalTuple<?>, Integer> items = new HashMap<RelationalTuple<?>, Integer>();
	
	

}
