/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

package de.uniol.inf.is.odysseus.database.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * 
 * @author Dennis Geesen
 * Created at: 20.10.2011
 * @author Marco Grawunder 
 */
public class DatabaseConnectionDictionary {
	
	
	public DatabaseConnectionDictionary(){
	}
	

	static private HashMap<String, IDatabaseConnectionFactory> factories = new HashMap<String, IDatabaseConnectionFactory>();
	static private Map<String, IDatabaseConnection> connections = new HashMap<String, IDatabaseConnection>();
	static private List<IDatabaseConnectionDictionaryListener> listeners = new ArrayList<IDatabaseConnectionDictionaryListener>();
	

	static public IDatabaseConnectionFactory getFactory(String dbms){
		dbms = dbms.toUpperCase();
		return factories.get(dbms);		
	}

	public void add(IDatabaseConnectionFactory factory) {
		factories.put(factory.getDatabase().toUpperCase(), factory);
		fireChangeEvent();
	}
	
	public void removeFactory(IDatabaseConnectionFactory factory){

		factories.remove(factory.getDatabase().toUpperCase());
		fireChangeEvent();
	}
	
	static public Set<String> getConnectionFactoryNames(){
		return factories.keySet();
	}

	static public Map<String, IDatabaseConnection> getConnections() {
		return connections;
	}

	static public void addConnection(String name, IDatabaseConnection connection){		
		name = name.toUpperCase();
		connections.put(name, connection);
		fireChangeEvent();
	}
	
	static public void removeConnection(String name){
		name = name.toUpperCase();
		IDatabaseConnection con = connections.remove(name);
		try {
			con.getConnection().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		fireChangeEvent();
	}
	
	static public void removeAllConnections(){
		for (Entry<String, IDatabaseConnection> c: connections.entrySet()){
			try {
				c.getValue().getConnection().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		connections.clear();
		fireChangeEvent();
	}
	
	static public boolean isConnectionExisting(String name){
		name = name.toUpperCase();
		return connections.containsKey(name);
	}
	
	static public IDatabaseConnection getDatabaseConnection(String name){
		name = name.toUpperCase();
		return connections.get(name);
	}
	
	static public void addListener(IDatabaseConnectionDictionaryListener listener){
		listeners .add(listener);
	}
	
	static public void removeListener(IDatabaseConnectionDictionaryListener listener){
		listeners.remove(listener);
	}
	
	static private void fireChangeEvent(){
		for(IDatabaseConnectionDictionaryListener listener : listeners){
			listener.databaseConnectionDictionaryChanged();
		}
	}
}
