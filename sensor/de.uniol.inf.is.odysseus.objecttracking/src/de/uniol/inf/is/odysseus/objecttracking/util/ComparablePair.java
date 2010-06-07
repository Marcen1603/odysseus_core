package de.uniol.inf.is.odysseus.objecttracking.util;

import de.uniol.inf.is.odysseus.base.IClone;

public class ComparablePair<T1 extends IClone, T2 extends IClone> implements Comparable<ComparablePair>{

	private T1 key;
	private T2 value;
	
	private int priority;
	
	public ComparablePair(T1 key, T2 value){
		this.key = key;
		this.value = value;
		this.priority = 0;
	}
	
	private ComparablePair(ComparablePair<T1, T2> original) {
		this.key = (T1)original.key.clone();
		this.value = (T2)original.value.clone();
	}

	@Override
	public int compareTo(ComparablePair o) {
		if(o.getPriority() < this.getPriority()){
			return -1;
		}else if(o.getPriority() == this.getPriority()){
			return 0;
		}else if(o.getPriority()> this.getPriority()){
			return 1;
		}
		return 0;
	}
	
	public ComparablePair<T1, T2> clone() {
		return new ComparablePair<T1, T2>(this);
	}
	
	public T1 getKey(){
		return this.key;
	}
	
	public T2 getValue(){
		return this.value;
	}
	
	public void increasePriority(){
		this.priority++;
	}
	
	public void decreasePriority(){
		this.priority--;
	}
	
	public void setPriority(int newPriority){
		this.priority = newPriority;
	}
	
	private int getPriority(){
		return priority;
	}	
	
}
