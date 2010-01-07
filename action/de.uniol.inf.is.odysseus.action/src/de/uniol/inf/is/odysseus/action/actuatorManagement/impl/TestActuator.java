package de.uniol.inf.is.odysseus.action.actuatorManagement.impl;

import de.uniol.inf.is.odysseus.action.actuatorManagement.ActuatorAdapter;

public class TestActuator extends ActuatorAdapter {
	private String name;
	private byte adress;
	private double result;
	
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
	
	public void doSomething (byte a, double b, double c, int d){
		this.result = a+b+c+d;
	}
	
	public double giveSomething () {
		return this.result;
	}
	
	
}
