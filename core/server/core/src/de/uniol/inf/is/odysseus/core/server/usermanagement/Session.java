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

import de.uniol.inf.is.odysseus.core.usermanagement.AbstractSession;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class Session extends AbstractSession implements ISession {

	private static final long serialVersionUID = -3066873062845084076L;


	public Session(IUser user, ITenant tenant) {
		super(user, tenant);
	}

	
	@Override
	protected long getSessionTimeout() {
		// TODO: Currently, there are problems with long running queries and a session timeout
		return 0;
		//		return SESSION_TIMEOUT;
	}
	
}
