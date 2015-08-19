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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
@LogicalOperator(name = "LDAPSOURCE", minInputPorts = 1, maxInputPorts = 1, doc = "This operator can write data to a LDAP directory tree.", category = { LogicalOperatorCategory.SINK,
        LogicalOperatorCategory.DATABASE })
public class LDAPSourceAO extends AbstractLDAPOperator {

    /**
     * 
     */
    private static final long serialVersionUID = 5478006463934248553L;
    private String filter;

    /**
     * Class constructor.
     *
     * @param operator
     */
    public LDAPSourceAO(LDAPSourceAO operator) {
        super(operator);
    }

    @Parameter(type = StringParameter.class, name = "filter", optional = true)
    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return this.filter;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public LDAPSourceAO clone() {
        return new LDAPSourceAO(this);
    }

}
