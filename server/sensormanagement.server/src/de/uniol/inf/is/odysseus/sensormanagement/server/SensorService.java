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

package de.uniol.inf.is.odysseus.sensormanagement.server;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.exception.InvalidUserDataException;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.XmlMarshalHelper;


@WebService
@SOAPBinding(style = Style.DOCUMENT)
public class SensorService 
{
	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory.getLogger(SensorService.class); 

	// Duplicated from de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.WebServiceServer
	private ISession loginWithSecurityToken(String securityToken) throws InvalidUserDataException 
	{
		ISession session = SessionManagement.instance.login(securityToken);
		if (session == null) 
			throw new InvalidUserDataException("Security token unknown! You have to login first to obtain a security token!");
		
		return session;
	}	

	public void initService(@WebParam(name = "securityToken") String securityToken,
							@WebParam(name = "name") String name,
							@WebParam(name = "loggingDirectory") String loggingDirectory) throws InvalidUserDataException
	{
		loginWithSecurityToken(securityToken);		
		SensorFactory.getInstance().init(name, loggingDirectory);
	}
	
	
	public String addSensor(@WebParam(name = "securityToken") String securityToken, 
						@WebParam(name = "sensorXml") String sensorXml) throws InvalidUserDataException
	{
		ISession session = loginWithSecurityToken(securityToken);		
		return SensorFactory.getInstance().addSensor(session, XmlMarshalHelper.fromXml(sensorXml, SensorModel.class)).config.id;
	}

	public void modifySensor(@WebParam(name = "securityToken") String securityToken, 
							@WebParam(name = "sensorId") String sensorId, 
							@WebParam(name = "sensorXml") String sensorXml) throws InvalidUserDataException
	{
		ISession session = loginWithSecurityToken(securityToken);		
		SensorFactory.getInstance().modifySensor(session, sensorId, XmlMarshalHelper.fromXml(sensorXml, SensorModel.class));
	}
	
	@WebResult(name = "sensorIds")
	public List<String> getSensorIds(@WebParam(name = "securityToken") String securityToken) throws InvalidUserDataException
	{
		loginWithSecurityToken(securityToken);		
		return SensorFactory.getInstance().getSensorIds();
	}	

	@WebResult(name = "sensorXml")
	public String getSensorById(@WebParam(name = "securityToken") String securityToken, 
								@WebParam(name = "sensorId") String sensorId) throws InvalidUserDataException
	{
		loginWithSecurityToken(securityToken);		
		return XmlMarshalHelper.toXml(SensorFactory.getInstance().getSensorById(sensorId).config);
	}		
	
	@WebResult(name = "sensorTypes")
	public List<String> getSensorTypes(@WebParam(name = "securityToken") String securityToken) throws InvalidUserDataException
	{
		loginWithSecurityToken(securityToken);		
		return SensorFactory.getInstance().getSensorTypes();
	}	
	
	@WebResult(name = "sensorTypeXml")
	public String getSensorType(@WebParam(name = "securityToken") String securityToken, 
								@WebParam(name = "sensorType") String sensorType) throws InvalidUserDataException
	{
		loginWithSecurityToken(securityToken);		
		return XmlMarshalHelper.toXml(SensorFactory.getInstance().getSensorType(sensorType));
	}			
	
	public void removeSensor(@WebParam(name = "securityToken") String securityToken, 
							@WebParam(name = "sensorId") String sensorId) throws InvalidUserDataException
	{
		ISession session = loginWithSecurityToken(securityToken);		
		SensorFactory.getInstance().removeSensor(session, sensorId);
	}
	
	public void startLogging(@WebParam(name = "securityToken") String securityToken, 
							@WebParam(name = "sensorId") String sensorId,
							@WebParam(name = "loggingStyle") String loggingStyle) throws InvalidUserDataException
	{
		ISession session = loginWithSecurityToken(securityToken);		
		SensorFactory.getInstance().startLogging(session, sensorId, loggingStyle);
	}

/*	public void startLogging(@WebParam(name = "securityToken") String securityToken, 
			@WebParam(name = "sensorId") String sensorId) throws InvalidUserDataException
	{
		ISession session = loginWithSecurityToken(securityToken);		
		SensorFactory.getInstance().startLogging(session, sensorId, Sensor.DEFAULT_LOGGING_STYLE);
	}*/
	
	
	public void stopLogging(@WebParam(name = "securityToken") String securityToken, 
							@WebParam(name = "sensorId") String sensorId,
							@WebParam(name = "loggingStyle") String loggingStyle) throws InvalidUserDataException
	{
		ISession session = loginWithSecurityToken(securityToken);		
		SensorFactory.getInstance().stopLogging(session, sensorId, loggingStyle);
	}	

	public void stopAllLogging(@WebParam(name = "securityToken") String securityToken, 
			@WebParam(name = "sensorId") String sensorId) throws InvalidUserDataException
	{
		ISession session = loginWithSecurityToken(securityToken);		
		SensorFactory.getInstance().stopAllLogging(session, sensorId);
	}	
	
	@WebResult(name = "streamUrl")
	public String startLiveView(@WebParam(name = "securityToken") String securityToken, 
							  @WebParam(name = "sensorId") String sensorId,
							  @WebParam(name = "targetHost") String targetHost, 
							  @WebParam(name = "targetPort") int targetPort) throws InvalidUserDataException
	{
		ISession session = loginWithSecurityToken(securityToken);		
		return SensorFactory.getInstance().startLiveView(session, sensorId, targetHost, targetPort);
	}

	public void stopLiveView(@WebParam(name = "securityToken") String securityToken, 
							@WebParam(name = "sensorId") String sensorId) throws InvalidUserDataException
	{
		ISession session = loginWithSecurityToken(securityToken);		
		SensorFactory.getInstance().stopLiveView(session, sensorId);
	}	
	
	
}
