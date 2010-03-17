package de.uniol.inf.is.odysseus.broker.transaction;

public abstract class AbstractPortTuple {
	
	int[] ports = new int[0];
	
	public AbstractPortTuple(int count){
		this.ports = new int[count];
	}
	
	public void setPort(int number, int port){
		this.ports[number] = port;
	}
	
	public int getPort(int number){
		return this.ports[number];
	}
}
