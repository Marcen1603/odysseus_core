package de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces;

import java.util.List;

import org.osgi.service.prefs.BackingStoreException;

import de.uniol.inf.is.odysseus.dbIntegration.model.DBProperties;


/**
 * Das Interface IConnection bietet die Moeglichkeit, Informationen zu Datenbanken 
 * im System zu hinterlegen. Dazu muss nur der entsprechende Service konsumiert werden.
 * 
 * @author crolfes
 *
 */
public interface IConnectionData {
	
	
	/**
	 * Hinzufuegen einer Verbindungsinstanz.
	 * @param properties 
	 * @throws BackingStoreException
	 */
	public void addConnection(DBProperties properties) throws BackingStoreException;
	
	
	/**
	 * 
	 * @param properties
	 * @throws BackingStoreException
	 */
	public void deleteConnection(String database) throws BackingStoreException;
	
	
	/**
	 * 
	 * @param properties
	 * @throws BackingStoreException
	 */
	public boolean connectionExists(String database) throws BackingStoreException;
	
	
	/**
	 * 
	 * @param properties
	 * @throws BackingStoreException
	 */
	public DBProperties getConnection(String database) throws BackingStoreException;
	
	
	/**
	 * 
	 * @param properties
	 * @throws BackingStoreException
	 */
	public void updateConnection(DBProperties properties) throws BackingStoreException;
	
	
	/**
	 * 
	 * @param properties
	 * @throws BackingStoreException
	 */
	public List<DBProperties> getAllConnections() throws BackingStoreException;
}
