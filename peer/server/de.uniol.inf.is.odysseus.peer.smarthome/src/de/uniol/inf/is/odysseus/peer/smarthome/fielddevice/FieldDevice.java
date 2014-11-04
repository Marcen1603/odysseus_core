package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.util.List;

public interface FieldDevice {
	public String getName();
	
	public List<String> getPossibleActivityNames();
	//public void addPossibleActivityName(String string);
}
