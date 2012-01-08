/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @deprecated Replaced by {@link IUser}
 * 
 */
@Deprecated
public final class User extends AbstractUserManagementEntity implements
		Serializable, Comparable<User> {

	private static final long serialVersionUID = -6085280063468701069L;
	private final String hashFunction = "SHA-1";
	private String password;
	private Session session;
	private List<Role> roles;
	static transient MessageDigest hash = null;

	User(String username, String password) {
		setName(username);
		this.password = hash(password);
		this.roles = new ArrayList<Role>();
	}

	/**
	 * Adds a role to a user.
	 * 
	 * @param role
	 * @deprecated Replaced by
	 *             {@link IUserManagement#grantRole(IUser, IRole, ISession)}
	 */
	@Deprecated
	void addRole(Role role) {
		if (role != null && !this.roles.contains(role)) {
			this.roles.add(role);
		}
	}

	@Override
	public int compareTo(User o) {
		return this.getUsername().compareTo(o.getUsername());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.hashCode() == obj.hashCode())
			return true;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * @deprecated Replaced by
	 *             {@link IUserManagement#findRole(String, ISession)}
	 */
	@Deprecated
	public Role getRoleByName(String name) {
		for (Role role : roles) {
			if (role.getRolename().equals(name)) {
				return role;
			}
		}
		return null;
	}

	/**
	 * Returns a list of all roles contained in the user.
	 * 
	 * @return List<Role>
	 */
	public List<Role> getRoles() {
		return this.roles;
	}

	public Session getSession() {
		return this.session;
	}

	public String getUsername() {
		return getName();
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
				+ ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}

	/**
	 * Return the corresponding Role if the user has privileges on the given
	 * role
	 * 
	 * @param rolename
	 * @return Role
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

	/**
	 * Removes a role from a user.
	 * 
	 * @see de.uniol.inf.is.odysseus.usermanagement.Role.java
	 * @param Role
	 */
	void removeRole(Role role) {
		this.roles.remove(role);
	}

	/**
	 * @deprecated Replaced by
	 *             {@link IUserManagement#changePassword(IUser, byte[], ISession)}
	 */
	@Deprecated
	public void setPassword(String password) {
		this.password = hash(password);
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void setUsername(String username) {
		setName(username);
	}

	@Override
	public String toString() {
		return getName();
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
