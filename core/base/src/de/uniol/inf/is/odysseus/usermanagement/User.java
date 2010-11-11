package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public final class User extends AbstractUserManagementEntity implements Serializable,
		Comparable<User> {

	private static final long serialVersionUID = -6085280063468701069L;
	private final String hashFunction = "SHA-1";
	private String username;
	private String password;
	private Session session;
	private boolean active;
	private List<Role> roles;
	static transient MessageDigest hash = null;

	User(String username, String password) {
		this.active = true;
		this.username = username;
		this.password = hash(password);
		this.roles = new ArrayList<Role>();
	}

	void addRole(Role role) {
		if (!this.roles.contains(role)) {
			this.roles.add(role);
		}
	}

	@Override
	public int compareTo(User o) {
		return this.getUsername().compareTo(o.getUsername());
	}

	protected void deaktivateUser() {
		this.active = false;
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

	public String getPassword() {
		return password;
	}

	public Role getRoleByName(String name) {
		for (Role role : roles) {
			if (role.getRolename().equals(name)) {
				return role;
			}
		}
		return null;
	}

	public List<Role> getRoles() {
		return this.roles;
	}

	public Session getSession() {
		return this.session;
	}

	public String getUsername() {
		return username;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	/**
	 * return the corresponding Role if the user has privileges on the given
	 * role
	 * 
	 * @param rolename
	 * @return
	 */
	public Role hasRole(String rolename) {
		for (Role role : getRoles()) {
			if (role.getRolename().equals(rolename)) {
				return role;
			}
		}
		return null;
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

	public boolean isActiv() {
		return this.active;
	}

	void removeRole(Role role) {
		this.roles.remove(role);
	}

	public void setPassword(String password) {
		this.password = hash(password);
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return username;// + " " + password;
	}

	public boolean validatePassword(String password, boolean passwordIsHash) {
		if (passwordIsHash) {
			return password.equals(this.password);
		} else {
			String h = hash(password);
			return this.password.equals(h);
		}
	}

}
