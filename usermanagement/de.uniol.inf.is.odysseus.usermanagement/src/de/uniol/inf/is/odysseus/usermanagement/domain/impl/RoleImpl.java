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

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.uniol.inf.is.odysseus.usermanagement.domain.Role;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
@Entity(name = "Role")
@Table(name = "Role", uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME"})})
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries(value = {@NamedQuery(name = RoleImpl.NQ_FIND_ALL, query = "select o from Role as o"),
                       @NamedQuery(name = RoleImpl.NQ_FIND_BY_NAME, query = "select o from Role as o where o.name = :name"),})
public class RoleImpl extends AbstractEntityImpl<RoleImpl> implements Role {

    private static final long serialVersionUID = -3017359149581752836L;
    public static final String NQ_FIND_ALL = "de.uniol.inf.is.odysseus.usermanagement.domain.Role.findAll";
    public static final String NQ_FIND_BY_NAME = "de.uniol.inf.is.odysseus.usermanagement.domain.Role.findByName";
    private String name;
    @OneToMany
    private List<PrivilegeImpl> privileges;

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.usermanagement.domain.Role#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.usermanagement.domain.Role#getPrivileges()
     */
    @Override
    public List<PrivilegeImpl> getPrivileges() {
        return this.privileges;
    }

    public void addPrivilege(final PrivilegeImpl privilege) {
        this.privileges.add(privilege);
    }

    public void removePrivilege(final PrivilegeImpl privilege) {
        this.privileges.remove(privilege);
    }

    /**
     * @param privileges The privileges to set.
     */
    public void setPrivileges(final List<PrivilegeImpl> privileges) {
        this.privileges = privileges;
    }

}
