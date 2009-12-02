package de.uniol.inf.is.odysseus.dbIntegration.dataAccess;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;

import de.uniol.inf.is.odysseus.dbIntegration.model.DBProperties;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IConnectionData;

public class ConnectionData implements IConnectionData {
	
	private PreferencesService preferencesService;
	private final String url = "URL";
	private final String schema = "SCHEMA";
	private final String user = "USER";
	private final String password = "PASSWORD";
	private final String driver = "DRIVER";
	private final String db = "db";
	


	@Override
	public void addConnection(DBProperties properties) {
		@SuppressWarnings("unused")
		Preferences test = preferencesService.getSystemPreferences();
		Preferences preferences = preferencesService.getSystemPreferences().node("DATABASE");
		
			
			try {
				if(!preferences.nodeExists(properties.getDatabase())) {
					Preferences tmp = preferences.node(properties.getDatabase());
					tmp.put(db, properties.getDatabase());
					tmp.put(driver, properties.getDriverClass());
					tmp.put(schema, properties.getSchema());
					tmp.put(url, properties.getUrl());
					tmp.put(user, properties.getUser());
					tmp.put(password, properties.getPassword());
					preferences.flush();
				} else  {
					//TODO 
					System.out.println("verbindung existiert");
				}
			} catch (BackingStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		
		

	}

	@Override
	public boolean connectionExists(String database) throws BackingStoreException {
		Preferences preferences = preferencesService.getSystemPreferences().node("DATABASE");
		return preferences.nodeExists(database);
	}

	@Override
	public void deleteConnection(String database) throws BackingStoreException {
		Preferences preferences = preferencesService.getSystemPreferences().node("DATABASE");
		preferences.node(database).removeNode();
		preferences.flush();

	}

	

	@Override
	public List<DBProperties> getAllConnections() throws BackingStoreException {
		Preferences preferences = preferencesService.getSystemPreferences().node("DATABASE");
		List<DBProperties> props = new ArrayList<DBProperties>();
		for (String name : preferences.childrenNames()) {
			Preferences tmpPref = preferences.node(name);
			props.add(new DBProperties(
					tmpPref.get(db, ""),
					tmpPref.get(url, ""),
					tmpPref.get(driver, ""),
					tmpPref.get(user, ""),
					tmpPref.get(password, ""),
					tmpPref.get(schema, "")
			));
		}
		return props;
	}

	@Override
	public DBProperties getConnection(String database) throws BackingStoreException {
		Preferences preferences = preferencesService.getSystemPreferences().node("DATABASE");
		if (!preferences.nodeExists(database)) {
			return null;
		}
		Preferences tmpPref = preferences.node(database);
		DBProperties prop =  new DBProperties(
				tmpPref.get(db, ""),
				tmpPref.get(url, ""),
				tmpPref.get(driver, ""),
				tmpPref.get(user, ""),
				tmpPref.get(password, ""),
				tmpPref.get(schema, "")
				)
		;
		return prop;
	}

	@Override
	public void updateConnection(DBProperties properties) {
		Preferences preferences = preferencesService.getSystemPreferences().node("DATABASE");
		Preferences tmp = preferences.node(properties.getDatabase());
		//TODO: update connection implementieren

	}
	
	protected void setPreferencesService(PreferencesService preferencesService) {
		this.preferencesService = preferencesService;
	}

	protected void unsetPreferencesService(PreferencesService preferencesService) {
		this.preferencesService = null;
	}

}
