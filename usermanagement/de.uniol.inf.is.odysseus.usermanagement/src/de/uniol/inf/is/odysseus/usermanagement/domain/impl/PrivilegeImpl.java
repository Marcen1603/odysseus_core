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

import de.uniol.inf.is.odysseus.usermanagement.domain.Permission;
import de.uniol.inf.is.odysseus.usermanagement.domain.Privilege;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
@Entity(name = "Privilege")
@Table(name = "Privilege")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries(value = {@NamedQuery(name = PrivilegeImpl.NQ_FIND_ALL, query = "select o from Privilege as o"),
                       @NamedQuery(name = PrivilegeImpl.NQ_FIND_BY_OBJECTURI, query = "select o from Privilege as o where o.objectURI = :objectURI"),})
public class PrivilegeImpl extends AbstractEntityImpl<PrivilegeImpl> implements Privilege {
    /** Find all privileges */
    public static final String NQ_FIND_ALL = "de.uniol.inf.is.odysseus.usermanagement.domain.Privilege.findAll";
    /** Find all privileges for one object */
    public static final String NQ_FIND_BY_OBJECTURI = "de.uniol.inf.is.odysseus.usermanagement.domain.Privilege.findByObjectURI";

    private static final long serialVersionUID = 4054608803558374338L;
    private String objectURI;

    @ElementCollection
    @CollectionTable(name = "Privilege_Permission")
    private Set<Permission> permissions = new HashSet<Permission>();

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.domain.Privilege#getPermissions()
     */
    @Override
    public Set<Permission> getPermissions() {
        return this.permissions;
    }

    /**
     * @param operations The operations to set.
     */
    public void setPermissions(final Set<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * @param permission
     */
    public void addPermission(final Permission permission) {
        this.permissions.add(permission);
    }

    /**
     * @param permission
     */
    public void removePermission(final Permission permission) {
        this.permissions.remove(permission);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.domain.Privilege#getObjectURI()
     */
    @Override
    public String getObjectURI() {
        return this.objectURI;
    }

    /**
     * @param objectURI The objectURI to set.
     */
    public void setObjectURI(final String objectURI) {
        this.objectURI = objectURI;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uniol.inf.is.odysseus.usermanagement.domain.Privilege#getOwner()
     */
    @Override
    public void getOwner() {

    }

}
