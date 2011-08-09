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

package de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.Endpoint;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.exception.WebserviceException;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

/**
 * 
 * @author Dennis Geesen Created at: 09.08.2011
 */

@WebService
@SOAPBinding(style = Style.RPC)
public class WebserviceServer {

	public static void startServer() {
		WebserviceServer server = new WebserviceServer();
		Endpoint endpoint = Endpoint.publish("http://localhost:9669/odysseus", server);
		if (endpoint.isPublished()) {
			Logger.getAnonymousLogger().log(Level.FINE, "Webservice published!");
		}

	}

	public WebserviceResponse<String> login(String username, String password) {
		User user = UserManagement.getInstance().login(username, password, false);
		if (user != null) {
			String token = SessionManagement.getInstance().createNewSession(user);
			WebserviceResponse<String> response = new WebserviceResponse<String>(token, true);
			return response;
		} else {
			return new WebserviceResponse<String>(null, false);
		}
	}

	public WebserviceResponse<Boolean> addQuery(String securityToken, String parser, String query, String transCfg) {		
		try {
			User user = loginWithSecurityToken(securityToken);
			List<IQueryBuildSetting<?>> cfg = ExecutorServiceBinding.getExecutor().getQueryBuildConfiguration(transCfg);
			IDataDictionary dd = GlobalState.getActiveDatadictionary();
			ExecutorServiceBinding.getExecutor().addQuery(query, parser, user, dd, cfg.toArray(new IQueryBuildSetting[0]));			
			return new WebserviceResponse<Boolean>(true, true);
		} catch (WebserviceException e) {
			e.printStackTrace();
		} catch (PlanManagementException e) {			
			e.printStackTrace();
		}
		return new WebserviceResponse<Boolean>(false, false);

	}

	private User loginWithSecurityToken(String securityToken) throws WebserviceException {
		if(SessionManagement.getInstance().isValidSession(securityToken)){
			User user = SessionManagement.getInstance().getUser(securityToken);			
			return user;
		}else {
			throw new WebserviceException("Security token unknown! You have to login first to obtain a security token!");
		}
	}

}
