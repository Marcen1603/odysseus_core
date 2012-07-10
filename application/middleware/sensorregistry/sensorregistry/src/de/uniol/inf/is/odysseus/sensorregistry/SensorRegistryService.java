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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Endpoint;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.WebserviceServer;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.response.StringResponse;


/**
 * 
 * @author Dennis Geesen Created at: 16.08.2011
 */
@WebService
@SOAPBinding(style = Style.DOCUMENT)
@XmlSeeAlso({ SensorSchema.class, SensorAttribute.class, StringResponse.class })
public class SensorRegistryService extends WebserviceServer {

	private static Logger LOG = LoggerFactory.getLogger(SensorRegistryService.class); 
	private static final String TRANSFORMATION_CONFIGURATION = "Standard";

	public static void startServer() {
		SensorRegistryService server = new SensorRegistryService();
		Endpoint endpoint = Endpoint.publish("http://0.0.0.0:9999/odysseus", server);
		if (endpoint.isPublished()) {
			LOG.debug("Webservice published!");
		}

	}
	
	public boolean isSensorRegistered(@WebParam(name ="securityToken") String securityToken, @WebParam(name = "name") String name ){
		try{
			loginWithSecurityToken(securityToken);
			return SensorRegistry.getInstance().isSensorRegistered(name);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean registerSensor(@WebParam(name = "securityToken") String securityToken, @WebParam(name = "name") String name, @WebParam(name = "host") String host,
			@WebParam(name = "port") int port, @WebParam(name = "schema") SensorSchema schema) {
		try {
			ISession user = loginWithSecurityToken(securityToken);			
			
			Sensor sensor = new Sensor(host, port);
			boolean result = SensorRegistry.getInstance().registerSensor(name, sensor);
			if (result) {
				LOG.debug("Sensor " + name + " registered");
				String query = "CREATE STREAM " + name + "(" + buildParamList(schema) + ") CHANNEL " + host + " : " + port;
				LOG.debug("Creating Stream in Odysseus: " + query);
				getExecutor().addQuery(query, "CQL", user, TRANSFORMATION_CONFIGURATION);				
			}else{
				LOG.debug("Sensor "+name+" was already registered");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static String buildParamList(SensorSchema schema) {
		String s = "";
		String sep = "";
		for (SensorAttribute a : schema.getAttributes()) {
			s = s + sep + " " + a.getName() + " " + a.getType().toUpperCase();
			sep = ",";
		}
		return s;

	}

	public boolean unregisterSensor(@WebParam(name = "securityToken") String securityToken, @WebParam(name = "name") String name) {
		try {
			ISession user = loginWithSecurityToken(securityToken);
			
			boolean result = SensorRegistry.getInstance().unregisterSensor(name);
			if (result) {				
				LOG.debug("Sensor " + name + " unregistered");
				String query = "DROP STREAM " + name;
				LOG.debug("Creating Stream in Odysseus: " + query);
				getExecutor().addQuery(query, "CQL", user, TRANSFORMATION_CONFIGURATION);				
			}else{
				LOG.debug("Sensor "+name+" was never registered");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
