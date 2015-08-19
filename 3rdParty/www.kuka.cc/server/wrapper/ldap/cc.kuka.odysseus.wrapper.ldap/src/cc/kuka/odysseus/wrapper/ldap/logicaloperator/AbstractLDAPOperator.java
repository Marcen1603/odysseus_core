/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.wrapper.ldap.logicaloperator;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.ldap.LdapContext;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public abstract class AbstractLDAPOperator extends AbstractLogicalOperator {
    /**
     * 
     */
    private static final long serialVersionUID = -7084852655307818779L;
    private final String initCtxFactory = "com.sun.jndi.ldap.LdapCtxFactory";
    private final String controlFactory = "com.sun.jndi.ldap.ControlFactory";

    private String host;
    private String user;
    private String password;
    private String base;

    /**
     * Class constructor.
     *
     */
    public AbstractLDAPOperator() {
        super();
    }

    /**
     * Class constructor.
     *
     */
    public AbstractLDAPOperator(AbstractLDAPOperator operator) {
        super(operator);
        this.host = operator.getHost();
        this.user = operator.getUser();
        this.password = operator.getPassword();
    }

    @Parameter(type = StringParameter.class, name = "user", optional = true)
    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return this.user;
    }

    @Parameter(type = StringParameter.class, name = "password", optional = true)
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    @Parameter(type = StringParameter.class, name = "base", optional = true)
    public void setBase(String base) {
        this.base = base;
    }

    public String getBase() {
        return this.base;
    }
    @Parameter(type = StringParameter.class, name = "host", optional = true)
    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return this.host;
    }

    public Hashtable<Object, Object> getEnvironment() {
        Hashtable<Object, Object> environment = new Hashtable<>();
        environment.put(LdapContext.CONTROL_FACTORIES, this.controlFactory);
        environment.put(Context.INITIAL_CONTEXT_FACTORY, this.initCtxFactory);
        environment.put(Context.PROVIDER_URL, getHost());
        if (!"".equals(getUser())) {
            environment.put(Context.SECURITY_AUTHENTICATION, "simple");
            environment.put(Context.SECURITY_PRINCIPAL, getUser());
            environment.put(Context.SECURITY_CREDENTIALS, getPassword());
        }
        else {
            environment.put(Context.SECURITY_AUTHENTICATION, "none");

        }
        return environment;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public boolean isValid() {
        boolean isValid = super.isValid();

        return isValid;
    }

}
