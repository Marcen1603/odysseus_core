package de.uniol.inf.is.odysseus.action.actuator.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestActuator {
	private String name;
	private byte adress;
	private double result;
	private Logger logger;
	
	public TestActuator (String name){
		this.name = name;	
		this.logger = LoggerFactory.getLogger(TestActuator.class);
	}
	
	public TestActuator (String name, byte adress){
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
