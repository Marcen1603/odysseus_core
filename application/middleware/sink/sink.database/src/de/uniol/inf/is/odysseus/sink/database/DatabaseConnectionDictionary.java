/** Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package de.uniol.inf.is.odysseus.sink.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * 
 * @author Dennis Geesen
 * Created at: 20.10.2011
 */
public class DatabaseConnectionDictionary {
	
	
	public enum DatabaseType {
		Integer, String, Float, Long, Double, Boolean
	};	
	
	private static DatabaseConnectionDictionary instance;
	
	private DatabaseConnectionDictionary(){
		initMappings();
	}
	
	public static synchronized DatabaseConnectionDictionary getInstance(){
		if(instance == null){
			instance = new DatabaseConnectionDictionary();
		}
		return instance;
	}

	private HashMap<String, IDatabaseConnectionFactory> factories = new HashMap<String, IDatabaseConnectionFactory>();
	private Map<SDFDatatype, DatabaseType> datatypeMappings = new HashMap<SDFDatatype, DatabaseType>();
	private Map<String, IDatabaseConnection> connections = new HashMap<String, IDatabaseConnection>();
	private List<IDatabaseConnectionDictionaryListener> listeners = new ArrayList<IDatabaseConnectionDictionaryListener>();
	
	private void initMappings() {
		this.datatypeMappings.put(SDFDatatype.INTEGER, DatabaseType.Integer);
		this.datatypeMappings.put(SDFDatatype.BOOLEAN, DatabaseType.Boolean);
		this.datatypeMappings.put(SDFDatatype.END_TIMESTAMP, DatabaseType.Long);
		this.datatypeMappings.put(SDFDatatype.FLOAT, DatabaseType.Float);
		this.datatypeMappings.put(SDFDatatype.LONG, DatabaseType.Long);
		this.datatypeMappings.put(SDFDatatype.POINT_IN_TIME, DatabaseType.Long);
		this.datatypeMappings.put(SDFDatatype.START_TIMESTAMP, DatabaseType.Long);
		this.datatypeMappings.put(SDFDatatype.STRING, DatabaseType.String);
		this.datatypeMappings.put(SDFDatatype.TIMESTAMP, DatabaseType.Long);
		this.datatypeMappings.put(SDFDatatype.DOUBLE, DatabaseType.Double);

	}
	
	public DatabaseType getDatabaseType(SDFDatatype datatype){
		return this.datatypeMappings.get(datatype);
	}
	
	public List<SDFDatatype> getSDFDatatype(DatabaseType dbType){
		List<SDFDatatype> dts = new ArrayList<SDFDatatype>();
		for(Entry<SDFDatatype, DatabaseType> e : this.datatypeMappings.entrySet()){
			if(e.getValue().equals(dbType)){
				dts.add(e.getKey());
			}
		}
		return dts;
	}
			
	public Map<SDFDatatype, DatabaseType> getDatatypeMappings() {
		return datatypeMappings;
	}

	public IDatabaseConnectionFactory getFactory(String dbms){
		dbms = dbms.toUpperCase();
		return this.factories.get(dbms);		
	}

	public void addFactory(String dbms, IDatabaseConnectionFactory factory) {
		dbms = dbms.toUpperCase();
		this.factories.put(dbms, factory);
		this.fireChangeEvent();
	}
	
	public Set<String> getConnectionFactoryNames(){
		return this.factories.keySet();
	}

	public Map<String, IDatabaseConnection> getConnections() {
		return connections;
	}

	public void addConnection(String name, IDatabaseConnection connection){		
		name = name.toUpperCase();
		this.connections.put(name, connection);
		this.fireChangeEvent();
	}
	
	public boolean isConnectionExisting(String name){
		name = name.toUpperCase();
		return this.connections.containsKey(name);
	}
	
	public IDatabaseConnection getDatabaseConnection(String name){
		name = name.toUpperCase();
		return this.connections.get(name);
	}
	
	public void addListener(IDatabaseConnectionDictionaryListener listener){
		this.listeners .add(listener);
	}
	
	public void removeListener(IDatabaseConnectionDictionaryListener listener){
		this.listeners.remove(listener);
	}
	
	private void fireChangeEvent(){
		for(IDatabaseConnectionDictionaryListener listener : this.listeners){
			listener.databaseConnectionDictionaryChanged();
		}
	}
}
