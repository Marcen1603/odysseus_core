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

package de.uniol.inf.is.soop.sensorregistry.webservice;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.Endpoint;

import de.uniol.inf.is.soop.sensorregistry.webservice.response.StringResponse;

/**
 * 
 * @author Jan-Ole Brode Created at: 25.02.2012
 */

@WebService
@SOAPBinding(style = Style.DOCUMENT)

public class WebserviceServer {

	public static void startServer() {
		WebserviceServer server = new WebserviceServer();		
		Endpoint endpoint = Endpoint.publish("http://0.0.0.0:9677/sensor", server);
		if (endpoint.isPublished()) {
			System.out.println("SensorRegistry published!");
			Logger.getAnonymousLogger().log(Level.FINE, "SensorRegistry published!");
		}
	}
	/**
	 * get datastream for query
	 * @param consumerProcessId
	 * @param contextVariable
	 * @param frequency
	 * @return streamId
	 * TODO implement scai config call
	 */
	@WebResult(name = "streamId")
	public StringResponse getStream(
			@WebParam(name = "instanceId") String instanceId, 
			@WebParam(name = "contextVariable") String contextVariable,
			@WebParam(name = "frequency") String frequency
	){
		
			StringResponse response = new StringResponse("OfficerSensor", true);
			return response;
		
	}
	/**
	 * release subscription
	 * @param consumerProcessId
	 * @param StreamId
	 * @return
	 */
	@WebResult(name = "successful")
	public StringResponse releaseStream(
			@WebParam(name = "consumerProcessId") String consumerProcessId, 
			@WebParam(name = "streamId") String StreamId
	) {
		
			StringResponse response = new StringResponse("OfficerSensor", true);
			return response;
		
	}

}
