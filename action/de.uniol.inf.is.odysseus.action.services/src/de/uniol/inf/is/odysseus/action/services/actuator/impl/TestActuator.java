package de.uniol.inf.is.odysseus.action.services.actuator.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.action.services.actuator.ActuatorAdapter;

public class TestActuator extends ActuatorAdapter {
	private String name;
	private byte adress;
	private double result;
	private Logger logger;
	
	public TestActuator (String name){
		super();
		this.name = name;	
		this.logger = LoggerFactory.getLogger(TestActuator.class);
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
	
	public void doSomething(byte a, double b, double c, int d){
		this.result = a+b+c+d;
	}
	
	public double giveSomething() {
		return this.result;
	}
	
	public void setFields(String name, byte adress){
		this.name = name;
		this.adress = adress;
		this.logger.debug(this.name+":"+adress);
	}
	
	
}
