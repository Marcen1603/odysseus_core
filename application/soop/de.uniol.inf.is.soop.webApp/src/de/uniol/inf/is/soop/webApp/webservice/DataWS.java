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

package de.uniol.inf.is.soop.webApp.webservice;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.Endpoint;

import de.uniol.inf.is.soop.webApp.controlWsClient.StringResponse;
import de.uniol.inf.is.soop.webApp.impl.MonitorServlet;
import de.uniol.inf.is.soop.webApp.session.Session;
import de.uniol.inf.is.soop.webApp.session.SessionManager;

/**
 * 
 * @author Jan-Ole Brode Created at: 25.02.2012
 */

@WebService
@SOAPBinding(style = Style.DOCUMENT)

public class DataWS {

	private static MonitorServlet websocket = MonitorServlet.getInstance();
	
	public static void startServer() {
		DataWS server = new DataWS();		
		Endpoint endpoint = Endpoint.publish("http://0.0.0.0:9671/dataws", server);
		if (endpoint.isPublished()) {
			Logger.getAnonymousLogger().log(Level.FINE, "Control Webservice published on 9671!");
		}
		
	}
	/**
	 * get token for session
	 * @param username
	 * @param password
	 * @return token
	 */
	@WebResult(name = "token")
	public StringResponse pushData(
			@WebParam(name = "token") String token, 
			@WebParam(name = "payload") String payload
	){
		
		Session session;
		
		StringResponse response = null;
		
		try {
			websocket.pushData(payload);
			
		} catch (Exception e) {
			response = new StringResponse(false, e.getMessage());
			e.printStackTrace();
		}
		
			return response;
	}
	

}
