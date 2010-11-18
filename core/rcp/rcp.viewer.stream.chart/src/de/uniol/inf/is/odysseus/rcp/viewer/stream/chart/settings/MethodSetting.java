package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings;

import java.lang.reflect.Method;

public class MethodSetting {

	private Method getter;
	private Method setter;
	private String name;
	
	public MethodSetting(String name, Method getter, Method setter) {
		this.getter = getter;
		this.setter = setter;
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public Method getGetter() {
		return getter;
	}

	public void setGetter(Method getter) {
		this.getter = getter;
	}

	public Method getSetter() {
		return setter;
	}

	public void setSetter(Method setter) {
		this.setter = setter;
	}
	
	
}
