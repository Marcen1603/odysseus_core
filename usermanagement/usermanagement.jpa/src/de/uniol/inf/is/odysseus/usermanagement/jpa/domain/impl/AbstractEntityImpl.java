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

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import de.uniol.inf.is.odysseus.usermanagement.IAbstractEntity;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
@MappedSuperclass
abstract public class AbstractEntityImpl<T> implements Serializable, IAbstractEntity {
    @Transient
    private static final long serialVersionUID = 8557204806069271649L;

    @Id
    @Column(length = 150)
    protected String id = UUID.randomUUID().toString();
    @Version
    protected Long version;

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.domain.AbstractEntity#getId()
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * @param id
     */
    public void setId(final String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.usermanagement.domain.AbstractEntity#getVersion
     * ()
     */
    @Override
    public Long getVersion() {
        return this.version;
    }

    /**
     * @param version
     */
    public void setVersion(final Long version) {
        this.version = version;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += this.id != null ? this.id.hashCode() : 0;
        return hash;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof AbstractEntityImpl<?>)) {
            return false;
        }
        if (!this.getClass().isInstance(object)) {
            return false;
        }
        final AbstractEntityImpl<?> other = (AbstractEntityImpl<?>)object;
        if (this.id == null && other.id != null || this.id != null && !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "[id=" + this.id + "]";
    }
}
