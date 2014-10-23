package de.uniol.inf.is.odysseus.core.usermanagement;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;


public abstract class AbstractSession implements ISession {

	private static final long serialVersionUID = -4806265953390081384L;
	private final String id = UUID.randomUUID().toString();
	private final IUser user;
	private final ITenant tenant;
	private final long start;
	private long end;
	private String token = "";
	private String connectionName = "";

	public AbstractSession(final IUser user, final ITenant tenant) {
		this.user = user;
		this.tenant = tenant;
		start = System.currentTimeMillis();
		end = start + getSessionTimeout();
	}
	
	abstract protected long getSessionTimeout();

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
		if (getSessionTimeout() > 0) {
			long timestamp = System.currentTimeMillis();
			return timestamp >= start && timestamp <= end;
		} else {
			return true;
		}
	}

	@Override
	public void updateSession() {
		if (isValid()) {
			this.end = System.currentTimeMillis() + getSessionTimeout();
		}
	}

	@Override
	public String getToken() {
		if (token.isEmpty()) {
			token = createToken();
		}
		return token;
	}

	protected void setToken(String token){
		this.token = token;
	}
	
	private static String createToken() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

	@Override
	public String getConnectionName() {
		return connectionName;
	}
	
	@Override
	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}
	
	@Override
	public String toString() {
		return tenant + " " + user + " " + end;
	}
}
