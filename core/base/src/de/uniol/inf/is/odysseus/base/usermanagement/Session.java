package de.uniol.inf.is.odysseus.base.usermanagement;

import java.io.Serializable;

public class Session implements Serializable{

	private static final long serialVersionUID = 6601485682039247781L;
	final int key;
	
	Session(int key){
		this.key = key;
	}
}
