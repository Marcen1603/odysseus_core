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

package de.uniol.inf.is.soop.control.webservice;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.Endpoint;

import org.omg.CORBA.UserException;

import de.uniol.inf.is.soop.control.dsms.DatastreamManagementSystem;
import de.uniol.inf.is.soop.control.usermanagement.Session;
import de.uniol.inf.is.soop.control.usermanagement.SessionManager;
import de.uniol.inf.is.soop.control.usermanagement.User;
import de.uniol.inf.is.soop.control.usermanagement.UserManager;
import de.uniol.inf.is.soop.control.usermanagement.Usergroup;
import de.uniol.inf.is.soop.control.webservice.response.StringResponse;
import de.uniol.inf.is.soop.control.workflow.WorkflowProcess;
import de.uniol.inf.is.soop.control.workflow.WorkflowProcessInstance;

/**
 * 
 * @author Jan-Ole Brode Created at: 25.02.2012
 */

@WebService
@SOAPBinding(style = Style.DOCUMENT)

public class SOOPControlWS {

	public static void startServer() {
		SOOPControlWS server = new SOOPControlWS();		
		Endpoint endpoint = Endpoint.publish("http://0.0.0.0:9670/control", server);
		if (endpoint.isPublished()) {
			Logger.getAnonymousLogger().log(Level.FINE, "Control Webservice published on 9670!");
		}
	}
	/**
	 * get token for session
	 * @param username
	 * @param password
	 * @return token
	 */
	@WebResult(name = "token")
	public StringResponse login(
			@WebParam(name = "username") String username, 
			@WebParam(name = "password") String password
	){
		
		Session newSession;
		
		StringResponse response = null;
		
		try {
			newSession = SessionManager.getInstance().createSession(username, password);
			response = new StringResponse(newSession.getToken(), true);
			
		} catch (Exception e) {
			response = new StringResponse(e.getMessage(), false);
			e.printStackTrace();
		}
		
			return response;
	}
	
	
	/**
	 * add user to SOOPControl
	 * @param token
	 * @param username
	 * @param password
	 * @param groups
	 * @return
	 */
	@WebResult(name = "userId")
	public StringResponse addUser(
			@WebParam(name = "token") String token, 
			@WebParam(name = "username") String username, 
			@WebParam(name = "realname") String realname,
			@WebParam(name = "password") String password, 
			@WebParam(name = "usergroups") String usergroups
	) {
			StringResponse response = null;
			
			Session s = SessionManager.getInstance().getSessionByToken(token);
			
			if (s instanceof Session){
				
				// check user permission
				if (s.getUser().getGroups().containsKey("admins")) {
					UserManager um = UserManager.getInstance();
					
					HashMap<String, Usergroup> groupsList = new HashMap<String, Usergroup>();
					
					String[] groupNames = usergroups.split(",");
					for(String g : groupNames){
						// check if usergroup exists
						if (um.getUsergroups().containsKey(g)){
							// add user to exisiting group
							groupsList.put(g, um.getUsergroup(g));
						} else {
							
						}
					}
					
					// superusers may create new users
					User u = new User(username, realname, password, groupsList);
					
					// add new user to list, if username is already used, the old user will be overwritten
					UserManager.getInstance().addUser(u);
					
				} else {
					// user is not authorized
					
				}
				HashMap<String, WorkflowProcess> activeProcesses = s.getWfEngine().getProcesses();
				
				
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				XMLEncoder xmlEncoder = new XMLEncoder(baos);
				xmlEncoder.writeObject(activeProcesses);
				xmlEncoder.close();

				String out = baos.toString();
				
				
				response = new StringResponse(out, true);
			} else {
				response = new StringResponse("addUser failed", false);
			}
		
			return response;
		
	}
	
	
	
	/**
	 * release subscription
	 * @param token
	 * @param workflowEngineId
	 * @return
	 */
	@WebResult(name = "processList")
	public StringResponse getInstalledProcesses(
			@WebParam(name = "token") String token, 
			@WebParam(name = "workflowEngineId") String workflowEngineId
	) {
			StringResponse response = null;
			
			Session s = SessionManager.getInstance().getSessionByToken(token);
			
			if (s instanceof Session){
				
				HashMap<String, WorkflowProcess> activeProcesses = s.getWfEngine().getProcesses();
				
				
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				XMLEncoder xmlEncoder = new XMLEncoder(baos);
				xmlEncoder.writeObject(activeProcesses);
				xmlEncoder.close();

				String out = baos.toString();
				
				
				response = new StringResponse(out, true);
			} else {
				response = new StringResponse("auth failed", false);
			}
		
			return response;
		
	}
	
	/**
	 * release subscription
	 * @param consumerProcessId
	 * @param StreamId
	 * @return
	 */
	@WebResult(name = "instanceId")
	public StringResponse startProcess(
			@WebParam(name = "token") String token, 
			@WebParam(name = "processId") String processId
	) {

		StringResponse response = null;
		
		Session s = SessionManager.getInstance().getSessionByToken(token);
		
		if (s instanceof Session){
			
			WorkflowProcess p = s.getWfEngine().getWorkflowProcess(processId);
			
			WorkflowProcessInstance instance = null;
			
			if (p instanceof WorkflowProcess){
				instance = p.getAdapter().startProcess(s.getWfEngine(), s.getDsms(), s.getSensorRegistry());
				p.addInstance(instance);
			}
			
			response = new StringResponse("Created instance of process "+ processId + " with instanceId: " + instance.getPid(), true);
		} else {
			response = new StringResponse("auth failed", false);
		}
	
		return response;
		
	}
	
	/**
	 * send signal to change missin phase
	 * @param token
	 * @param instanceId
	 * @return
	 */
	@WebResult(name = "instanceId")
	public StringResponse forwardButtonPressed(
			@WebParam(name = "token") String token, 
			@WebParam(name = "processId") String instanceId /* wrong name -> is instanceId*/
	) {

		StringResponse response = null;
		
		Session s = SessionManager.getInstance().getSessionByToken(token);
		
		if (s instanceof Session){
			
			WorkflowProcess p = s.getWfEngine().getWorkflowProcess("stepOverProcess");
			
			if (p instanceof WorkflowProcess){
				p.getAdapter().Signal__leave_step(instanceId);
			}
			
			response = new StringResponse("forward instance of process "+ "stepOverProcess" + " with instanceId: " + instanceId, true);
		} else {
			response = new StringResponse("auth failed", false);
		}
	
		return response;
		
	}

}
