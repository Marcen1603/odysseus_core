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
package cc.kuka.odysseus.misc.function.miscellaneous;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class DewPointFunction extends AbstractFunction<Double> {
    /**
     *
     */
    private static final long serialVersionUID = -9075036598384499962L;
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public DewPointFunction() {
        super("dewPoint", 2, DewPointFunction.ACC_TYPES, SDFDatatype.DOUBLE);
    }

    @Override
    public Double getValue() {
        final double temperature = this.getNumericalInputValue(0).doubleValue();
        final double humidity = this.getNumericalInputValue(1).doubleValue();

        // (1) Saturation Vapor Pressure = ESGG(T)
        final double RATIO = 373.15 / (273.15 + temperature);
        double RHS = -7.90298 * (RATIO - 1);
        RHS += 5.02808 * Math.log10(RATIO);
        RHS += -1.3816e-7 * (Math.pow(10, (11.344 * (1 - (1 / RATIO)))) - 1);
        RHS += 8.1328e-3 * (Math.pow(10, (-3.49149 * (RATIO - 1))) - 1);
        RHS += Math.log10(1013.246);

        // factor -3 is to adjust units - Vapor Pressure SVP * humidity
        final double VP = Math.pow(10, RHS - 3) * humidity;

        // (2) DEWPOINT = F(Vapor Pressure)
        final double T = Math.log(VP / 0.61078); // temp var
        return new Double((241.88 * T) / (17.558 - T));
    }
}
