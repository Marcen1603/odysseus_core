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

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
@LogicalOperator(name = "LDAPSINK", minInputPorts = 1, maxInputPorts = 1, doc = "This operator can write data to a LDAP directory tree.", category = { LogicalOperatorCategory.SINK,
        LogicalOperatorCategory.DATABASE })
public class LDAPSinkAO extends AbstractLDAPOperator {

    /**
     * 
     */
    private static final long serialVersionUID = 3335367339683122624L;

    /**
     * Class constructor.
     *
     * @param operator
     */
    public LDAPSinkAO(LDAPSinkAO operator) {
        super(operator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LDAPSinkAO clone() {
        return new LDAPSinkAO(this);
    }

}
