/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.util;

public abstract class NamedElement {

	public String name = null;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
