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
package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.client.util;

import de.uniol.inf.is.odysseus.core.usermanagement.AbstractSession;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

/**
 * 
 * @author Marco Grawunder
 *
 */

public class WsClientSession  extends AbstractSession implements ISession {


	private static final long serialVersionUID = 3523363132440028382L;
	private String tenantName;

	public WsClientSession(final IUser user, final String tenant, final String connection) {
		super(user, null);
		this.tenantName = tenant;
		setConnectionName(connection);
	}
	
	@Override
	protected long getSessionTimeout() {
		// Is not relevant on client side		
		return 0;
	}
	
	public String getTenantName() {
		return tenantName;
	}

	@Override
	public void setToken(String securitytoken) {
		super.setToken(securitytoken);
	}
		
}
