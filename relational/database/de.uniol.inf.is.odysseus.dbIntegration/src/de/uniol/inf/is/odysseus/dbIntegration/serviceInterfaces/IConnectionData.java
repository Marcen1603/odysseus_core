package de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces;

import java.util.List;

import org.osgi.service.prefs.BackingStoreException;

import de.uniol.inf.is.odysseus.dbIntegration.model.DBProperties;

public interface IConnectionData {
	
	public void addConnection(DBProperties properties) throws BackingStoreException;
	public void deleteConnection(String database) throws BackingStoreException;
	public boolean connectionExists(String database) throws BackingStoreException;
	public DBProperties getConnection(String database) throws BackingStoreException;
	public void updateConnection(DBProperties properties) throws BackingStoreException;
	public List<DBProperties> getAllConnections() throws BackingStoreException;
}
