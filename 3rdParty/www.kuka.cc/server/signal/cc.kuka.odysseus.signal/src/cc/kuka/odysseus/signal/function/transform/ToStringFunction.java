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
package cc.kuka.odysseus.signal.function.transform;

import cc.kuka.odysseus.signal.common.datatype.Complex;
import cc.kuka.odysseus.signal.common.sdf.schema.SDFSignalDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ToStringFunction extends AbstractFunction<String> {
    /**
     *
     */
    private static final long serialVersionUID = -5277166177167534610L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFSignalDatatype.COMPLEX } };

    /**
     *
     */
    public ToStringFunction() {
        super("toString", 1, ToStringFunction.accTypes, SDFDatatype.STRING);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        final Complex a = (Complex) this.getInputValue(0);
        return a.toString();
    }

    public static void main(final String[] args) {
        final Complex a = new Complex(1, 2);
        System.out.println(a.toString());
    }
}
