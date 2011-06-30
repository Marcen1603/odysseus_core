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
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.uniol.inf.is.odysseus.usermanagement.domain.User;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
@Entity
@Table(name = "Account")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries(value = {@NamedQuery(name = UserImpl.NQ_FIND_ALL, query = "select o from Account as o"),
                       @NamedQuery(name = UserImpl.NQ_FIND_BY_NAME, query = "select o from Account as o where o.name = :name"),})
public class UserImpl extends AbstractEntityImpl<UserImpl> implements User {

    public static final String NQ_FIND_ALL = "de.uniol.inf.is.odysseus.usermanagement.domain.User.findAll";
    public static final String NQ_FIND_BY_NAME = "de.uniol.inf.is.odysseus.usermanagement.domain.User.findByName";
    private static final long serialVersionUID = -5114286931356318036L;

    private String name;
    private String algorithm;
    private String password;
    private boolean active;
    @OneToMany
    private List<RoleImpl> roles;
    @OneToMany
    private List<PrivilegeImpl> privileges;

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(User other) {
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
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uniol.inf.is.odysseus.usermanagement.domain.User#getAlgorithm()
     */
    @Override
    public String getAlgorithm() {
        if ((this.algorithm == null) || ("".equals(this.algorithm))) {
            return "SHA-256";
        } else {
            return this.algorithm;
        }
    }

    /**
     * @param algorithm The algorithm to set.
     */
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * @param password
     */
    public void setPassword(byte[] password) {
        try {
            this.password = getPasswordDigest(getAlgorithm(), password).toString();
        } catch (NoSuchAlgorithmException e) {
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
     * @param active The active to set.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uniol.inf.is.odysseus.usermanagement.domain.User#getRoles()
     */
    @Override
    public List<RoleImpl> getRoles() {
        return roles;
    }

    /**
     * @param roles The roles to set.
     */
    public void setRoles(List<RoleImpl> roles) {
        this.roles = roles;
    }

    public void addRole(RoleImpl role) {
        this.roles.add(role);
    }

    public void removeRole(RoleImpl role) {
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
     * @param privileges The privileges to set.
     */
    public void setPrivileges(List<PrivilegeImpl> privileges) {
        this.privileges = privileges;
    }

    public void addPrivilege(PrivilegeImpl privilege) {
        this.privileges.add(privilege);
    }

    public void removePrivilege(PrivilegeImpl privilege) {
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
    public boolean validatePassword(byte[] password) {
        try {
            return Arrays.equals(this.password.getBytes(), getPasswordDigest(getAlgorithm(), password));
        } catch (NoSuchAlgorithmException e) {
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
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        UserImpl other = (UserImpl)obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    /**
     * @param algorithmName The name of the algorithm.
     * @param password The password as byte array.
     * @return The digist using the given algorithm.
     * @throws NoSuchAlgorithmException
     */
    private byte[] getPasswordDigest(String algorithmName, byte[] password) throws NoSuchAlgorithmException {
        MessageDigest algorithm = MessageDigest.getInstance(algorithmName);
        algorithm.reset();
        algorithm.update(password);
        byte messageDigest[] = algorithm.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
        }
        return messageDigest;
    }

}
