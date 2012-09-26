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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

/**
 * 
 * @author Merlin Wasmann
 *
 * copied from de.uniol.inf.is.odysseus.core.server.usermanagement.Session by Christian Kuka
 */

public class WsClientSession implements ISession {

	private final static long SESSION_TIMEOUT = 10 * 60000;
	private final String id = UUID.randomUUID().toString();
	private final IUser user;
	private final long start;
	private long end;
	private String token = "";
	
	public WsClientSession(final IUser user) {
		this.user = user;
		start = System.currentTimeMillis();
		end = start + SESSION_TIMEOUT;
	}
	
	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public IUser getUser() {
		return this.user;
	}

	@Override
	public boolean isValid() {
		long ts = System.currentTimeMillis();
		return ts >= start && ts <= end;
	}

	@Override
	public void updateSession() {
		if(isValid()) {
			this.end = System.currentTimeMillis() + SESSION_TIMEOUT;
		}
	}
	
	public void setToken(String tk) {
		this.token = tk;
	}

	@Override
	public String getToken() {
		if(token.isEmpty()) {
			token = createToken();
		}
		return token;
	}
	
	private static String createToken() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

}
