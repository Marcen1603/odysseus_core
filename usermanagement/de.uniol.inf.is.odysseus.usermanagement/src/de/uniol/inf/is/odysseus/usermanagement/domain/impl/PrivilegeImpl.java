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
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import de.uniol.inf.is.odysseus.usermanagement.domain.Privilege;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
@Entity
@Table(name = "Privilege")
@Inheritance(strategy = InheritanceType.JOINED)
public class PrivilegeImpl extends AbstractEntityImpl<PrivilegeImpl> implements Privilege {

    private static final long serialVersionUID = 4054608803558374338L;
    private String objectURI;

    @ElementCollection
    @CollectionTable(name = "PRIVILEGE_OPERATION")
    private Set<Operation> operations;

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.domain.Privilege#getOperations()
     */
    @Override
    public Set<Operation> getOperations() {
        return operations;
    }

    /**
     * @param operations The operations to set.
     */
    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }

    public void addOperation(Operation operation) {
        this.operations.add(operation);
    }

    public void removeOperation(Operation operation) {
        this.operations.remove(operation);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.domain.Privilege#getObjectURI()
     */
    @Override
    public String getObjectURI() {
        return objectURI;
    }

    /**
     * @param objectURI The objectURI to set.
     */
    public void setObjectURI(String objectURI) {
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
