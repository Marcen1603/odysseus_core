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

package de.uniol.inf.is.odysseus.sensormanagement.client.executor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.sensormanagement.client.InvalidUserDataException_Exception;
import de.uniol.inf.is.odysseus.sensormanagement.client.SensorService;
import de.uniol.inf.is.odysseus.sensormanagement.client.SensorServiceService;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorType;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.XmlMarshalHelper;

/**
 * 
 * @author Henrik Surm
 * 
 */
@SuppressWarnings(value = { "all" })
public class WsSensorClient
{
	private SensorService server;

	/**
	 * @param connectString
	 *            String: expected format is
	 *            wsdlLocation;serviceNamespace;service
	 * @throws MalformedURLException 
	 */
	public WsSensorClient(String connectString) throws MalformedURLException 
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

	public void init(ISession caller, String name, String loggingDirectory)
	{
		try	{
			server.initService(caller.getToken(), name, loggingDirectory);
		} catch (InvalidUserDataException_Exception e) {
			throw new PlanManagementException(e);				
		}
	}	
	
	public String addSensor(ISession caller, SensorModel sensor)
	{
		try	{
			return server.addSensor(caller.getToken(), XmlMarshalHelper.toXml(sensor));
		} catch (InvalidUserDataException_Exception e) {
			throw new PlanManagementException(e);				
		}
	}
	
	public void removeSensor(ISession caller, String sensorId)
	{
		try {
			server.removeSensor(caller.getToken(), sensorId);
		} catch (InvalidUserDataException_Exception e) {
			throw new PlanManagementException(e);				
		}
	}	

	public void modifySensor(ISession caller, String sensorId, SensorModel sensor)
	{
		try {
			server.modifySensor(caller.getToken(), sensorId, XmlMarshalHelper.toXml(sensor));
		} catch (InvalidUserDataException_Exception e) 	{
			throw new PlanManagementException(e);				
		}
	}
	
	public List<String> getSensorIds(ISession caller)
	{
		try	{
			return server.getSensorIds(caller.getToken());
		} catch (InvalidUserDataException_Exception e) {
			throw new PlanManagementException(e);				
		}
	}	
	
	public SensorModel getSensorById(ISession caller, String sensorId) 
	{
		try {
			String sensorXml = server.getSensorById(caller.getToken(), sensorId);
			return XmlMarshalHelper.fromXml(sensorXml, SensorModel.class);
		} catch (InvalidUserDataException_Exception e) {
			throw new PlanManagementException(e);				
		}	
	}			

	public List<String> getSensorTypes(ISession caller)
	{
		try	{
			return server.getSensorTypes(caller.getToken());
		} catch (InvalidUserDataException_Exception e) {
			throw new PlanManagementException(e);				
		}
	}	
	
	public SensorType getSensorType(ISession caller, String sensorType) 
	{
		try {
			String sensorXml = server.getSensorType(caller.getToken(), sensorType);
			return XmlMarshalHelper.fromXml(sensorXml, SensorType.class);
		} catch (InvalidUserDataException_Exception e) {
			throw new PlanManagementException(e);				
		}	
	}				
	
	public void startLogging(ISession caller, String sensorId, String loggingStyle)
	{
		try {
			server.startLogging(caller.getToken(), sensorId, loggingStyle);
		} catch (InvalidUserDataException_Exception e) 	{
			throw new PlanManagementException(e);				
		}
	}
	
	public void stopLogging(ISession caller, String sensorId, String loggingStyle)
	{
		try {
			server.stopLogging(caller.getToken(), sensorId, loggingStyle);
		} catch (InvalidUserDataException_Exception e) {
			throw new PlanManagementException(e);				
		}
	}	
	
	public void stopAllLogging(ISession caller, String sensorId)
	{
		try {
			server.stopAllLogging(caller.getToken(), sensorId);
		} catch (InvalidUserDataException_Exception e) {
			throw new PlanManagementException(e);				
		}
	}	
	
	
	public String startLiveView(ISession caller, String sensorId, String targetHost, int targetPort)
	{
		try {
			return server.startLiveView(caller.getToken(), sensorId, targetHost, targetPort);
		} catch (InvalidUserDataException_Exception e) {
			throw new PlanManagementException(e);				
		}
	}
	
	public void stopLiveView(ISession caller, String sensorId)
	{
		try {
			server.stopLiveView(caller.getToken(), sensorId);
		} catch (InvalidUserDataException_Exception e) {
			throw new PlanManagementException(e);				
		}
	}
}
