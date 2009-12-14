package de.uniol.inf.is.odysseus.action.output.impl;

import de.uniol.inf.is.odysseus.action.output.ActuatorAdapter;

public class TestActuator extends ActuatorAdapter {
	private String name;
	private byte adress;
	
	public TestActuator (String name){
		super();
		this.name = name;		
	}
	
	public TestActuator (String name, byte adress){
		super();
		this.name = name;
		this.adress = adress;
	}
	
	public byte getAdress() {
		return adress;
	}
	
	public String getName() {
		return name;
	}
	
	public static void main(String[] args) {
		TestActuator a = new TestActuator("a");
	}
}
