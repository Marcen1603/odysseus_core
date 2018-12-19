/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.core.planmanagement.executor;

import java.net.SocketAddress;
import java.util.List;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * 
 * @author Marco Grawunder
 *
 */

public interface IClientExecutor {

	/**
	 * Opens a connection (if necessary) and logs a user in and creates a session
	 * 
	 * @param username
	 *            The user name
	 * @param password
	 *            The password of the user
	 * @param tenantname
	 *            The tenant
	 * @return the session of the logged in user
	 */
	ISession login(String username, byte[] password, String tenantname, String host, int port, String connectionInfo);

	List<SocketAddress> getSocketConnectionInformation(int queryId, ISession caller);	
	
}
