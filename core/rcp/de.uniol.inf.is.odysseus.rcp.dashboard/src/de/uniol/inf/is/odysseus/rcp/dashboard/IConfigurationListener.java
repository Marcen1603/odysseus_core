package de.uniol.inf.is.odysseus.rcp.dashboard;

public interface IConfigurationListener {

	public void settingChanged( String settingName, Object oldValue, Object newValue );
	
}
