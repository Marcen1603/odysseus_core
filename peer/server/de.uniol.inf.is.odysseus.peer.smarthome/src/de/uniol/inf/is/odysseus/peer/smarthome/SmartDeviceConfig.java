package de.uniol.inf.is.odysseus.peer.smarthome;

import java.io.Serializable;

public class SmartDeviceConfig implements Serializable  {
	private static final long serialVersionUID = 1227650912610397431L;
	private String contextname;

	public String getContextname() {
		return contextname;
	}

	public void setContextname(String contextname) {
		this.contextname = contextname;
	}

	public void overwriteWith(SmartDeviceConfig other) {
		if(other.getContextname()!=null){
			setContextname(other.getContextname());
		}
	}
	
}
