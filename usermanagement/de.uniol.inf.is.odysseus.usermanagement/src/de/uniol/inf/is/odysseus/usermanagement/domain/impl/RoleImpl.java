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
import javax.persistence.OneToMany;

import de.uniol.inf.is.odysseus.usermanagement.domain.Privilege;
import de.uniol.inf.is.odysseus.usermanagement.domain.Role;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class RoleImpl extends AbstractEntityImpl<RoleImpl> implements Role {

    private static final long serialVersionUID = -3017359149581752836L;
    private String name;
    private boolean group;
    @OneToMany
    private List<PrivilegeImpl> privileges;

    /*
     * (non-Javadoc)
     *
     * @see de.uniol.inf.is.odysseus.usermanagement.domain.Role#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uniol.inf.is.odysseus.usermanagement.domain.Role#isGroup()
     */
    @Override
    public boolean isGroup() {
        return group;
    }

    /**
     * @param group The group to set.
     */
    public void setGroup(boolean group) {
        this.group = group;
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

    /**
     * @param privileges The privileges to set.
     */
    public void setPrivileges(List<PrivilegeImpl> privileges) {
        this.privileges = privileges;
    }

}
