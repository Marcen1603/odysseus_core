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

/**
 * 
 * 
 * 
 * @author Dennis Geesen
 * Created at: 16.08.2011
 */
public class Sensor {
	
	private String host;
	private int port;
	private String protocol;
	private Map<String, String> values = new HashMap<String, String>();
	
	public Sensor(){
		
	}
	
	public Sensor(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	public Sensor(String host, int port, String protocol){
		this.host = host;
		this.port = port;
		this.protocol = protocol;
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public Map<String, String> getValues() {
		return values;
	}
	public void setValues(Map<String, String> values) {
		this.values = values;
	}
	
	
	

}
