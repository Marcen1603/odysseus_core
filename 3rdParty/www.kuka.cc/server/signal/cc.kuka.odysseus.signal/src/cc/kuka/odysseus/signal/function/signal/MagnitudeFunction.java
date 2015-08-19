/*******************************************************************************
 * Copyright (C) 2015  Christian Kuka <christian@kuka.cc>
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
package cc.kuka.odysseus.signal.function.signal;

import cc.kuka.odysseus.signal.common.datatype.Complex;
import cc.kuka.odysseus.signal.common.sdf.schema.SDFSignalDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class MagnitudeFunction extends AbstractFunction<Double> {

    /**
     *
     */
    private static final long serialVersionUID = 3327260457073224799L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFSignalDatatype.COMPLEX } };

    /**
     *
     */
    public MagnitudeFunction() {
        super("magnitude", 1, ImaginaryFunction.accTypes, SDFDatatype.DOUBLE);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Double getValue() {
        final Complex a = (Complex) this.getInputValue(0);
        return Double.valueOf(Math.hypot(a.getReal(), a.getImaginary()));
    }

}
