package de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.model;

import java.util.Observable;

public class Valve extends Observable {
	
	private boolean isOpen;
	
	public Valve() {
		this.isOpen = false;
	}
	
	public boolean isOpen() {
		return this.isOpen;
	}
	
	public void open() {
		this.isOpen = true;
		this.setChanged();
		this.notifyObservers();
	}
	
	public void close() {
		this.isOpen = false;
		this.setChanged();
		this.notifyObservers();
	}

}
