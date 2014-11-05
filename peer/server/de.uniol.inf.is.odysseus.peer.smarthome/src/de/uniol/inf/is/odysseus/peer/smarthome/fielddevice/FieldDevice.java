package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.io.Serializable;

public abstract class FieldDevice implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String prefix;
	private String postfix;
	private SmartDevice smartDevice;

	public FieldDevice(String name, String prefix, String postfix) {
		this.setName(name);
		this.setPrefix(prefix);
		this.setPostfix(postfix);
	}
	
	public void setSmartDevice(SmartDevice smartDevice) {
		this.smartDevice = smartDevice;
	}

	public SmartDevice getSmartDevice(){
		return this.smartDevice;
	}
	
	private void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	public String getPostfix() {
		return this.postfix;
	}
	public String toString() {
		return getName();
	}

}
