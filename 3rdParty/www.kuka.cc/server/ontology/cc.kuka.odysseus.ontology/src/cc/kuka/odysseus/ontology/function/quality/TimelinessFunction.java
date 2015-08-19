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
package cc.kuka.odysseus.ontology.function.quality;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 * 
 */
public class TimelinessFunction extends AbstractFunction<Double> {

    /**
     * 
     */
    private static final long serialVersionUID = 4703040530419998760L;
    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public TimelinessFunction() {
        super("timeliness", 2, TimelinessFunction.ACC_TYPES, SDFDatatype.DOUBLE, false);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final Double getValue() {
        final PointInTime applicationTime = PointInTime.currentPointInTime();
        final PointInTime streamTime = new PointInTime(this.getNumericalInputValue(0).longValue());
        final double frequency = this.getNumericalInputValue(1).doubleValue();
        final PointInTime difference = applicationTime.minus(streamTime);
        double timeliness = (1.0 - (difference.getMainPoint() / (1000.0 / frequency)));
        if (timeliness < 0.0) {
            timeliness = 0.0;
        }
        return new Double(timeliness);
    }

}
