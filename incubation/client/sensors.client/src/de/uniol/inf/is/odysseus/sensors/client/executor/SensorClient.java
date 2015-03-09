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

package de.uniol.inf.is.odysseus.sensors.client.executor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.sensors.client.InvalidUserDataException_Exception;
import de.uniol.inf.is.odysseus.sensors.client.SensorService;
import de.uniol.inf.is.odysseus.sensors.client.SensorServiceService;
import de.uniol.inf.is.odysseus.sensors.types.SensorModel2;
import de.uniol.inf.is.odysseus.sensors.utilities.XmlMarshalHelper;

/**
 * 
 * @author Henrik Surm
 * 
 */
@SuppressWarnings(value = { "all" })
public class SensorClient// implements IExecutor, IClientExecutor, IOperatorOwner 
{
	private SensorService server;

	/**
	 * @param connectString
	 *            String: expected format is
	 *            wsdlLocation;serviceNamespace;service
	 * @throws MalformedURLException 
	 */
	public SensorClient(String connectString) throws MalformedURLException 
	{
		String[] subConnect = connectString.split(";");
		if (subConnect.length > 1 && subConnect.length < 4) 
		{
			server = new SensorServiceService(new URL(subConnect[0]), new QName(subConnect[1], subConnect[2])).getSensorServicePort();
		}
		else
			throw new MalformedURLException("\"" + connectString + "\" is not a valid WebService URL!");
	}


	public SensorService getServer() 
	{
		return server;
	}

/*	@Override
	public ISession login(String username, byte[] password, String tenant) {
		// TODO: use real names
		String securitytoken = getWebserviceServer(REMOVEME).login(username,
				new String(password), tenant).getResponseValue();
		if (securitytoken == null){
			return null;
		}
		IUser user = new WsClientUser(username, password, true);
		WsClientSession session = new WsClientSession(user, null, REMOVEME);
		session.setToken(securitytoken);
		ClientSessionStore.addSession(REMOVEME, session);
		fireUpdateEvent(IUpdateEventListener.SESSION);
		return session;
	}

	@Override
	public ISession login(String username, byte[] password) {
		String securitytoken = getWebserviceServer(REMOVEME).login2(username,
				new String(password)).getResponseValue();
		if (securitytoken == null){
			return null;
		}
		IUser user = new WsClientUser(username, password, true);
		WsClientSession session = new WsClientSession(user, null, REMOVEME);
		session.setToken(securitytoken);
		ClientSessionStore.addSession(REMOVEME, session);
		fireUpdateEvent(IUpdateEventListener.SESSION);
		return session;
	} */

	public void addSensor(ISession caller, SensorModel2 sensor)
	{
		try 
		{
			server.addSensor(caller.getToken(), XmlMarshalHelper.toXml(sensor));
		} 
		catch (InvalidUserDataException_Exception e) 
		{
			throw new PlanManagementException(e);				
		}
	}
	
	public void removeSensor(ISession caller, String name)
	{
		try 
		{
			server.removeSensor(caller.getToken(), name);
		} 
		catch (InvalidUserDataException_Exception e) 
		{
			throw new PlanManagementException(e);				
		}
	}	
	
	public List<String> getSensorIds(ISession caller)
	{
		try 
		{
			return server.getSensorIds(caller.getToken());
		} 
		catch (InvalidUserDataException_Exception e) 
		{
			throw new PlanManagementException(e);				
		}
	}	
	
	public SensorModel2 getSensorById(ISession caller, String sensorId) 
	{
		try 
		{
			String sensorXml = server.getSensorById(caller.getToken(), sensorId);
			return XmlMarshalHelper.fromXml(sensorXml, SensorModel2.class);
		} 
		catch (InvalidUserDataException_Exception e) 
		{
			throw new PlanManagementException(e);				
		}	
	}			
	
	public void startLogging(ISession caller, String sensorId)
	{
		try 
		{
			server.startLogging(caller.getToken(), sensorId);
		} 
		catch (InvalidUserDataException_Exception e) 
		{
			throw new PlanManagementException(e);				
		}
	}
	
	public void stopLogging(ISession caller, String sensorId)
	{
		try 
		{
			server.stopLogging(caller.getToken(), sensorId);
		} 
		catch (InvalidUserDataException_Exception e) 
		{
			throw new PlanManagementException(e);				
		}
	}	
	
	public String startLiveView(ISession caller, String sensorId, String targetHost, int targetPort)
	{
		try 
		{
			return server.startLiveView(caller.getToken(), sensorId, targetHost, targetPort);
		} 
		catch (InvalidUserDataException_Exception e) 
		{
			throw new PlanManagementException(e);				
		}
	}
	
	public void stopLiveView(ISession caller, String sensorId)
	{
		try 
		{
			server.stopLogging(caller.getToken(), sensorId);
		} 
		catch (InvalidUserDataException_Exception e) 
		{
			throw new PlanManagementException(e);				
		}
	}
}
