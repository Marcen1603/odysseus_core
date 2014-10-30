package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.util.List;

public abstract class Actor implements FieldDevice {

	private String name;

	public String getName() {
		return this.name;
	}

	@Override
	public String getRawSourceName() {
		return null;
	}

	@Override
	public List<String> getPossibleActivityNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPossibleActivityName(String string) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getActivitySourceName() {
		// TODO Auto-generated method stub
		return null;
	}
}
