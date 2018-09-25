package de.uniol.inf.is.odysseus.core.collection;

import java.util.ArrayList;

public class NamedList<E> extends ArrayList<E> {
	
	private static final long serialVersionUID = -2988628500875963634L;
	
	private String name;

	public NamedList(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {	
		return this.name+": "+super.toString();
	}
}
