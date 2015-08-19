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
public class MultinominalCoefficientFunction extends AbstractFunction<Long> {
    /**
     *
     */
    private static final long serialVersionUID = -3437809219874799026L;
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.VECTORS };

    public MultinominalCoefficientFunction() {
        super("choose", 2, MultinominalCoefficientFunction.ACC_TYPES, SDFDatatype.LONG);
    }

    @Override
    public Long getValue() {
        final int n = this.getNumericalInputValue(0).intValue();
        final double[] k = (double[]) this.getInputValue(1);
        return MultinominalCoefficientFunction.getValueInternal(n, k);
    }

    private static Long getValueInternal(final int n, final double[] k) {
        // Replace by binomialCoefficient() function.
        final long nFactorial = ArithmeticUtils.factorial(n);
        long product = 1l;
        for (final double element : k) {
            product *= ArithmeticUtils.factorial((int) element);
        }
        return new Long(nFactorial / product);

    }

    public static void main(final String[] args) {
        final String text = "MISSISSIPPI";
        final int n = text.length();
        // 1 M, 4 Is, 4 Ss, and 2 Ps
        final double[] k = new double[] { 1.0, 4.0, 4.0, 2.0 };
        System.out.println(MultinominalCoefficientFunction.getValueInternal(n, k) + " == 34650");
    }
}
