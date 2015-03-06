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

package de.uniol.inf.is.odysseus.sensors;

import java.util.HashMap;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.exception.InvalidUserDataException;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.StringResponse;
import de.uniol.inf.is.odysseus.sensors.types.SensorModel2;
import de.uniol.inf.is.odysseus.sensors.utilities.XmlMarshalHelper;


@WebService
@SOAPBinding(style = Style.DOCUMENT)
//@XmlSeeAlso({ SensorSchema.class, SensorAttribute.class, StringResponse.class })
public class SensorService 
{
	private static Logger LOG = LoggerFactory.getLogger(SensorService.class); 

	// Duplicated from de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.WebServiceServer
	protected ISession loginWithSecurityToken(String securityToken) throws InvalidUserDataException 
	{
		ISession session = UserManagementProvider.getSessionmanagement().login(securityToken);
		if (session == null) 
			throw new InvalidUserDataException("Security token unknown! You have to login first to obtain a security token!");
		
		return session;
	}	
	
	public String addSensor(@WebParam(name = "securityToken") String securityToken, @WebParam(name = "sensorXml") String sensorXml) throws InvalidUserDataException
	{
		ISession session = loginWithSecurityToken(securityToken);
		
		SensorFactory.getInstance().addSensor(session, new XmlMarshalHelper<>(SensorModel2.class).fromXml(sensorXml));
		
//		ExecutorServiceBinding.getExecutor().addQuery(name, "PQL", session, Context.empty());
		
		return "asd";
	}

	public void removeSensor(@WebParam(name = "securityToken") String securityToken, @WebParam(name = "name") String name) throws InvalidUserDataException
	{
		ISession session = loginWithSecurityToken(securityToken);
		
		ExecutorServiceBinding.getExecutor().removeQuery(name, session);
	}	
}
