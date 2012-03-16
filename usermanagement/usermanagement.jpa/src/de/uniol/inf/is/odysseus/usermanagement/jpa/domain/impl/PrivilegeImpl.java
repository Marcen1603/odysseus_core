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
package de.uniol.inf.is.odysseus.usermanagement.jpa.domain.impl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import de.uniol.inf.is.odysseus.core.usermanagement.IAbstractEntity;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.IPrivilege;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
@Entity(name = "Privilege")
@Table(name = "Privilege")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries(value = {
		@NamedQuery(name = PrivilegeImpl.NQ_FIND_ALL, query = "select o from Privilege as o"),
		@NamedQuery(name = PrivilegeImpl.NQ_FIND_BY_OBJECTURI, query = "select o from Privilege as o where o.objectURI = :objectURI"), })
public class PrivilegeImpl extends AbstractEntityImpl<PrivilegeImpl> implements
		IPrivilege {
	/** Find all privileges */
	public static final String NQ_FIND_ALL = "de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Privilege.findAll";
	/** Find all privileges for one object */
	public static final String NQ_FIND_BY_OBJECTURI = "de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Privilege.findByObjectURI";

	private static final long serialVersionUID = 4054608803558374338L;
	private String objectURI;

	@ElementCollection
	@CollectionTable(name = "Privilege_Permission")
	private Set<IPermission> permissions = new HashSet<IPermission>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Privilege#getPermissions()
	 */
	@Override
	public Set<IPermission> getPermissions() {
		return this.permissions;
	}

	/**
	 * @param operations
	 *            The operations to set.
	 */
	public void setPermissions(final Set<IPermission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @param permission
	 */
	@Override
    public void addPermission(final IPermission permission) {
		this.permissions.add(permission);
	}

	/**
	 * @param permission
	 */
	@Override
    public void removePermission(final IPermission permission) {
		this.permissions.remove(permission);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Privilege#getObjectURI()
	 */
	@Override
	public String getObjectURI() {
		return this.objectURI;
	}

	/**
	 * @param objectURI
	 *            The objectURI to set.
	 */
	@Override
    public void setObjectURI(final String objectURI) {
		this.objectURI = objectURI;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ObjectURI: ").append(getObjectURI()).append("\n")
				.append("Permissions:\n");
		for (IPermission permission : getPermissions()) {
			sb.append("\t").append(permission.toString()).append("\n");
		}

		return sb.toString();
	}

	@Override
	public void update(IAbstractEntity entity) {
		// TODO Auto-generated method stub
		
	}
}
