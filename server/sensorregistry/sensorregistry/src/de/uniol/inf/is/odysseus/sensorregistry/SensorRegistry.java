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

package de.uniol.inf.is.odysseus.sensorregistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author Dennis Geesen
 * Created at: 16.08.2011
 */
public class SensorRegistry {
	
	private Map<String, Sensor> registeredSensors = new HashMap<String, Sensor>();
	private static SensorRegistry instance = null;
	
	private SensorRegistry(){
	
	}
	
	public static synchronized SensorRegistry getInstance(){
		if(instance == null){
			instance = new SensorRegistry();
		}
		return instance;
	}
	
	public boolean isSensorRegistered(String name){
		return registeredSensors.containsKey(name);
	}
	
	public boolean isSensorRegsistered(String host, int port){		
		for(Entry<String, Sensor> e : registeredSensors.entrySet()){
			if(e.getValue().getHost().equals(host) && e.getValue().getPort() == port){
				return true;
			}
		}
		return false;
	}
	
	public boolean registerSensor(String name, Sensor s){
		if(!registeredSensors.containsKey(name)){
			this.registeredSensors.put(name, s);
			return true;
		}
        return false;
	}
	
	public boolean unregisterSensor(String name){
		if(registeredSensors.containsKey(name)){
			registeredSensors.remove(name);
			return true;
		}
        return false;
	}
	
	public void clear(){
		this.registeredSensors.clear();
	}
	

}
