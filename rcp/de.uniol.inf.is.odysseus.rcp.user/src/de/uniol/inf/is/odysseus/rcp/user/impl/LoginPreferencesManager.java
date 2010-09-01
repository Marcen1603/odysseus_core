package de.uniol.inf.is.odysseus.rcp.user.impl;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import de.uniol.inf.is.odysseus.rcp.user.IUserConstants;

public class LoginPreferencesManager {

	private static LoginPreferencesManager instance;
	
	private Preferences prefs;
	private Preferences loginPrefs;
	
	private LoginPreferencesManager() {
		prefs = new ConfigurationScope().getNode(IUserConstants.PLUGIN_ID);
		loginPrefs = prefs.node("login");
	}
	
	public static LoginPreferencesManager getInstance() {
		if( instance == null ) 
			instance = new LoginPreferencesManager();
		return instance;
	}
	
	public String getUsername() {
		return loginPrefs.get("username", "");
	}
	
	public String getPasswordMD5() {
		return loginPrefs.get("passwordMD5", "");
	}
	
	public void setUsername( String username ) {
		loginPrefs.put("username", username);
	}
	
	public void setPasswordMD5( String passwordMD5 ) {
		loginPrefs.put("passwordMD5", passwordMD5);
	}
	
	public boolean getAutoLogin() {
		try {
			return Boolean.valueOf(loginPrefs.get("autologin", "false"));
		} catch( Exception ex ) {
			return false;
		}
	}
	
	public void setAutoLogin( boolean autoLogin ) {
		loginPrefs.put("autologin", String.valueOf(autoLogin));
	}
	
	public void save() {
		try {
			// Forces the application to save the preferences
			prefs.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
}
