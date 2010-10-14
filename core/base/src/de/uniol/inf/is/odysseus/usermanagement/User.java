package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class User extends AbstractAccessControlObject implements Serializable, Comparable<User> {

	private static final long serialVersionUID = -6085280063468701069L;
	private final String hashFunction = "SHA-1";
	private String username;
	private String password;
	private Session session;
	private List<Role> roles;
	private List<Privilege> privileges;
	static transient MessageDigest hash = null;
	private boolean admin = false;

	User(String username, String password) {
		this.username = username;
		this.password = hash(password);
	}

	private void initHash() {
		try {
			synchronized (User.class) {
				if (hash == null) {
					hash = MessageDigest.getInstance(hashFunction);
				}
			}
		} catch (NoSuchAlgorithmException e) {
		}
	}

	private String hash(String password) {
		StringBuffer hexString = new StringBuffer();
		if (hash == null) {
			initHash();
		}
		if (hash != null) {
			synchronized (hash) {
				hash.reset();
				hash.update(password.getBytes());
				byte[] result = hash.digest();
				for (int i = 0; i < result.length; i++) {
					if (result[i] <= 15 && result[i] >= 0) {
						hexString.append("0");
					}
					hexString.append(Integer.toHexString(0xFF & result[i]));
				}
			}
			return hexString.toString();
		} else {
			return password;
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public boolean validatePassword(String password, boolean passwordIsHash) {
		if (passwordIsHash) {
			return password.equals(this.password);
		} else {
			String h = hash(password);
			return this.password.equals(h);
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = hash(password);
	}

	@Override
	public String toString() {
		return username + " " + password;
	}

	@Override
	public int compareTo(User o) {
		return this.getUsername().compareTo(o.getUsername());
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Session getSession() {
		return this.session;
	}

	void addRole(Role role) {
		if (!this.roles.contains(role)) {
			this.roles.add(role);
		}
	}

	void removeRole(Role role) {
		this.roles.remove(role);
	}

	List<Role> getRoles() {
		return this.roles;
	}

	void grantAdmin() {
		this.admin = true;
	}

	void revokeAdmin() {
		this.admin = false;
	}

	boolean isAdmin() {
		return admin;
	}

	/**
	 * return the corresponding Privilege if the user has privileges on the
	 * given object
	 * 
	 * @param obj
	 * @return
	 */
	public Privilege hasObject(Object obj) {
		for (Privilege priv : getPrivileges()) {
			if (priv.getObject().equals(obj)) {
				return priv;
			}
		}
		return null;
	}

	/**
	 * return the corresponding Privilege if the user has the given privileges
	 * 
	 * @param hasrole
	 * @return
	 */
	public Privilege hasPrivilege(Privilege haspriv) {
		for (Privilege priv : getPrivileges()) {
			if (priv.equals(haspriv)) {
				return priv;
			}
		}
		return null;
	}

	/**
	 * return the corresponding Role if the user has privileges on the given
	 * role
	 * 
	 * @param hasrole
	 * @return
	 */
	public Role hasRole(Role hasrole) {
		for (Role role : getRoles()) {
			if (role.equals(hasrole)) {
				return role;
			}
		}
		return null;
	}

	/**
	 * return the corresponding Role if the user has privileges on the given
	 * role
	 * 
	 * @param hasrole
	 * @return
	 */
	public Role hasPrivilegeInRole(Privilege haspriv) {
		// TODO was ist wenn das Privileg in meheren Rollen vorkommt ?
		for (Role role : getRoles()) {
			for (Privilege priv : role.getPrivileges()) {
				if (priv.equals(haspriv)) {
					return role;
				}
			}
		}
		return null;
	}

}
