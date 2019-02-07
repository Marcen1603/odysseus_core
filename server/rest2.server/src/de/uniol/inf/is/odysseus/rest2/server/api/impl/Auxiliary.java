package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import java.util.Optional;

import org.wso2.msf4j.Request;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.server.SecurityAuthInterceptor;

final class Auxiliary {
	private Auxiliary() {
	}

	public static Optional<ISession> session(Request request) {
		String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		return Optional.ofNullable(SessionManagement.instance.login(securityToken));
	}
}
