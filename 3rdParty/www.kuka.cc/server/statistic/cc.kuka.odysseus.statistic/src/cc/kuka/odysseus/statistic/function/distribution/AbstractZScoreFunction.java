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
package cc.kuka.odysseus.statistic.function.distribution;

import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
abstract public class AbstractZScoreFunction<T> extends AbstractFunction<T> {

    /**
     *
     */
    private static final long serialVersionUID = -6800832603255975149L;

    public AbstractZScoreFunction(final SDFDatatype[][] accTypes) {
        super("zscore", 3, accTypes, SDFDatatype.DOUBLE);
    }

    protected final static RealVector getValueInternal(final RealVector value, final RealVector mean, final double sigma) {
        return value.subtract(mean).mapDivide((sigma == 0.0 ? 1.0 : sigma));

    }
}
