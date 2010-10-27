package de.uniol.inf.is.odysseus.priority;

public class Priority implements IPriority{

	byte prio;
	
	public Priority(){
		this.prio = 0;
	}
	
	private Priority(Priority original){
		this.prio = original.prio;
	}
	
	@Override
	public byte getPriority(){
		return this.prio;
	}
	
	@Override
	public void setPriority(byte prio){
		this.prio = prio;
	}
	
	@Override
	public IPriority clone(){
		return new Priority(this);
	}
}
