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
package de.uniol.inf.is.odysseus.core.server.usermanagement;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class Session implements ISession {

	private static final long serialVersionUID = -509478572220332340L;
	private final static long SESSION_TIMEOUT = OdysseusConfiguration.getLong(
			"sessionTimeout", 10 * 60000);
	private final String id = UUID.randomUUID().toString();
	private final IUser user;
	private final ITenant tenant;
	private final long start;
	private long end;
	private String token = "";

	public Session(final IUser user, final ITenant tenant) {
		this.user = user;
		this.tenant = tenant;
		start = System.currentTimeMillis();
		end = start + SESSION_TIMEOUT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session#getId
	 * ()
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session#getUser
	 * ()
	 */
	@Override
	public IUser getUser() {
		return this.user;
	}

	@Override
	public ITenant getTenant() {
		return this.tenant;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session#isValid
	 * ()
	 */
	@Override
	public boolean isValid() {
		if (SESSION_TIMEOUT > 0) {
			long timestamp = System.currentTimeMillis();
			return timestamp >= start && timestamp <= end;
		} else {
			return true;
		}
	}

	@Override
	public void updateSession() {
		if (isValid()) {
			this.end = System.currentTimeMillis() + SESSION_TIMEOUT;
		}
	}

	@Override
	public String getToken() {
		if (token.isEmpty()) {
			token = createToken();
		}
		return token;
	}

	private static String createToken() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

	@Override
	public String toString() {
		return tenant + " " + user + " " + end;
	}
}
