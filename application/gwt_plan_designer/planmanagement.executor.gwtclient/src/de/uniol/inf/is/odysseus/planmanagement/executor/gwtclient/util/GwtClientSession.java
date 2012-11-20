package de.uniol.inf.is.odysseus.planmanagement.executor.gwtclient.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * 
 * @author Merlin Wasmann
 *
 * copied from de.uniol.inf.is.odysseus.core.server.usermanagement.Session by Christian Kuka
 */

public class GwtClientSession {
	
	private final static long SESSION_TIMEOUT = 10 * 60000;
	private final String id = UUID.randomUUID().toString();
	private final GwtClientUser user;
	private final long start;
	private long end;
	private String token = "";
	
	public GwtClientSession(final GwtClientUser user) {
		this.user = user;
		start = System.currentTimeMillis();
		end = start + SESSION_TIMEOUT;
	}
	
	
	public String getId() {
		return this.id;
	}

	
	public GwtClientUser getUser() {
		return this.user;
	}

	
	public boolean isValid() {
		long ts = System.currentTimeMillis();
		return ts >= start && ts <= end;
	}

	
	public void updateSession() {
		if(isValid()) {
			this.end = System.currentTimeMillis() + SESSION_TIMEOUT;
		}
	}
	
	public void setToken(String tk) {
		this.token = tk;
	}

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

