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
package cc.kuka.odysseus.statistic.function;

import org.apache.commons.math3.util.ArithmeticUtils;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class BinominalCoefficientFunction extends AbstractFunction<Long> {

    /**
     *
     */
    private static final long serialVersionUID = -6514350369049840933L;
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public BinominalCoefficientFunction() {
        super("choose", 2, BinominalCoefficientFunction.ACC_TYPES, SDFDatatype.LONG);
    }

    @Override
    public Long getValue() {
        final int n = this.getNumericalInputValue(0).intValue();
        final int k = this.getNumericalInputValue(1).intValue();
        return BinominalCoefficientFunction.getValueInternal(n, k);
    }

    private static Long getValueInternal(final int n, final int k) {
        // Replace by binomialCoefficient() function.
        return new Long(ArithmeticUtils.binomialCoefficient(n, k));

    }

    public static void main(final String[] args) {
        final int n = 3;
        final int k = 2;
        System.out.println(BinominalCoefficientFunction.getValueInternal(n, k) + " == 3");
    }
}
