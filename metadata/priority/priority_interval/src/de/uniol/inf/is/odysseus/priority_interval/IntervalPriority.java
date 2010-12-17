package de.uniol.inf.is.odysseus.priority_interval;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class IntervalPriority extends TimeInterval implements IPriority {

	private static final long serialVersionUID = -313473603482217362L;
	private byte priority = 0;

	public IntervalPriority() {
	}
	
	public IntervalPriority(IntervalPriority other){
		super(other);
		this.priority = other.priority;
	}
	
	@Override
	public byte getPriority() {
		return this.priority;
	}

	@Override
	public void setPriority(byte priority) {
		this.priority = priority;
	}
	
	@Override
	public IntervalPriority clone() {
		return new IntervalPriority(this);
	}
	
	@Override
	public String toString() {
		return super.toString()+" p= "+this.priority;
	}
	
	@Override
	public String csvToString() {
		return super.csvToString()+";"+this.priority;
	}

}
