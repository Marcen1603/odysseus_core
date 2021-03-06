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
package de.uniol.inf.is.odysseus.datatype.interval.function.relation;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.datatype.interval.datatype.IntervalDouble;
import de.uniol.inf.is.odysseus.datatype.interval.sdf.schema.SDFIntervalDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class BeforeFunction extends AbstractFunction<Boolean> {

    /**
     *
     */
    private static final long serialVersionUID = 7609911062166575351L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFIntervalDatatype.TYPES, SDFIntervalDatatype.TYPES };

    public BeforeFunction() {
        super("before", 2, BeforeFunction.ACC_TYPES, SDFDatatype.BOOLEAN);
    }

    @Override
    public Boolean getValue() {
        final IntervalDouble a = this.getInputValue(0);
        final IntervalDouble b = this.getInputValue(1);
        return Boolean.valueOf(a.sup()< b.inf());

    }

}
