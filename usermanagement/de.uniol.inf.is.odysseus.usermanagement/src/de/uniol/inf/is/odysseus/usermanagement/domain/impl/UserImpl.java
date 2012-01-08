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
package de.uniol.inf.is.odysseus.usermanagement.domain.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.uniol.inf.is.odysseus.usermanagement.IPrivilege;
import de.uniol.inf.is.odysseus.usermanagement.IRole;
import de.uniol.inf.is.odysseus.usermanagement.IUser;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
@Entity(name = "User")
@Table(name = "Account", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries(value = {
		@NamedQuery(name = UserImpl.NQ_FIND_ALL, query = "select o from User as o"),
		@NamedQuery(name = UserImpl.NQ_FIND_BY_NAME, query = "select o from User as o where o.name = :name"), })
public class UserImpl extends AbstractEntityImpl<UserImpl> implements IUser {

	public static final String NQ_FIND_ALL = "de.uniol.inf.is.odysseus.usermanagement.domain.User.findAll";
	public static final String NQ_FIND_BY_NAME = "de.uniol.inf.is.odysseus.usermanagement.domain.User.findByName";
	private static final long serialVersionUID = -5114286931356318036L;

	private String name;
	private String algorithm;
	private String password;
	private boolean active;
	@OneToMany
	private List<RoleImpl> roles = new ArrayList<RoleImpl>();
	@OneToMany
	private List<PrivilegeImpl> privileges = new ArrayList<PrivilegeImpl>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final IUser other) {
		return this.getName().compareTo(other.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.usermanagement.domain.User#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public String getAlgorithm() {
		if (this.algorithm == null || "".equals(this.algorithm)) {
			return "SHA-256";
		} else {
			return this.algorithm;
		}
	}

	/**
	 * @param algorithm
	 *            The algorithm to set.
	 */
	public void setAlgorithm(final String algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * @param password
	 */
	public void setPassword(final byte[] password) {
		try {
			this.password = this.getPasswordDigest(this.getAlgorithm(),
					password).toString();
		} catch (final NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.usermanagement.domain.User#isActive()
	 */
	@Override
	public boolean isActive() {
		return this.active;
	}

	/**
	 * @param active
	 *            The active to set.
	 */
	public void setActive(final boolean active) {
		this.active = active;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.usermanagement.domain.User#getRoles()
	 */
	@Override
	public List<RoleImpl> getRoles() {
		return this.roles;
	}

	/**
	 * @param roles
	 *            The roles to set.
	 */
	public void setRoles(final List<RoleImpl> roles) {
		this.roles = roles;
	}

	/**
	 * @param role
	 */
	public void addRole(final RoleImpl role) {
		this.roles.add(role);
	}

	/**
	 * @param role
	 */
	public void removeRole(final RoleImpl role) {
		this.roles.remove(role);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.usermanagement.domain.User#getPrivileges()
	 */
	@Override
	public List<PrivilegeImpl> getPrivileges() {
		return this.privileges;
	}

	/**
	 * @param privileges
	 *            The privileges to set.
	 */
	public void setPrivileges(final List<PrivilegeImpl> privileges) {
		this.privileges = privileges;
	}

	/**
	 * @param privilege
	 */
	public void addPrivilege(final PrivilegeImpl privilege) {
		this.privileges.add(privilege);
	}

	/**
	 * @param privilege
	 */
	public void removePrivilege(final PrivilegeImpl privilege) {
		this.privileges.remove(privilege);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.domain.User#validatePassword(
	 * byte[])
	 */
	@Override
	public boolean validatePassword(final byte[] password) {
		try {
			return Arrays.equals(this.password.getBytes(),
					this.getPasswordDigest(this.getAlgorithm(), password));
		} catch (final NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ (this.name == null ? 0 : this.name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final UserImpl other = (UserImpl) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	/**
	 * @param algorithmName
	 *            The name of the algorithm.
	 * @param password
	 *            The password as byte array.
	 * @return The digist using the given algorithm.
	 * @throws NoSuchAlgorithmException
	 */
	private byte[] getPasswordDigest(final String algorithmName,
			final byte[] password) throws NoSuchAlgorithmException {
		final MessageDigest algorithm = MessageDigest
				.getInstance(algorithmName);
		algorithm.reset();
		algorithm.update(password);
		final byte messageDigest[] = algorithm.digest();

		final StringBuffer hexString = new StringBuffer();
		for (final byte element : messageDigest) {
			hexString.append(Integer.toHexString(0xFF & element));
		}
		return messageDigest;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: ").append(getName()).append("\n")
				.append("Privileges:\n");
		for (IPrivilege privilege : getPrivileges()) {
			sb.append("\t").append(privilege.toString()).append("\n");
		}
		sb.append("\n").append("Roles:\n");
		for (IRole role : getRoles()) {
			sb.append("\t").append(role.toString()).append("\n");
		}
		return sb.toString();
	}
}
