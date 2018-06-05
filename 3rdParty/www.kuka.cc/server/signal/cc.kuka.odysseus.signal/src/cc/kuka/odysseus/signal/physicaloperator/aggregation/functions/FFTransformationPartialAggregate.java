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
package cc.kuka.odysseus.signal.physicaloperator.aggregation.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.ArithmeticUtils;

import cc.kuka.odysseus.signal.common.datatype.Complex;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class FFTransformationPartialAggregate<R> extends AbstractPartialAggregate<R> {
    /**
     *
     */
    private static final long serialVersionUID = -3419723129433301870L;
    private final List<Number> values = new LinkedList<>();

    public FFTransformationPartialAggregate() {
    }

    public FFTransformationPartialAggregate(final Number a) {
        this.add(a);
    }

    public FFTransformationPartialAggregate(final FFTransformationPartialAggregate<R> partialAggregate) {
        this.values.addAll(partialAggregate.values);
    }

    public List<Complex> getAggValue() {
        final FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

        int size = this.values.size();
        if (!ArithmeticUtils.isPowerOfTwo(size)) {
            size = this.values.size() & (this.values.size() - 1);
        }

        if (size > 0) {
            final double[] primitiveValues = new double[size];
            for (int i = 0; i < size; i++) {
                primitiveValues[i] = this.values.get(i).doubleValue();
            }

            final org.apache.commons.math3.complex.Complex[] complex = fft.transform(primitiveValues, TransformType.FORWARD);
            final List<Complex> result = new ArrayList<>(complex.length);
            for (final org.apache.commons.math3.complex.Complex element : complex) {
                result.add(new Complex(element.getReal(), element.getImaginary()));
            }
            return result;
        }
        return Arrays.asList(new Complex[] { new Complex(Double.NaN, Double.NaN) });
    }

    public void add(final Number a) {
        if (a != null) {
            this.values.add(a);
        }
    }

    public void add(final FFTransformationPartialAggregate<?> value) {
        this.values.addAll(value.values);

    }

    @Override
    public FFTransformationPartialAggregate<R> clone() {
        return new FFTransformationPartialAggregate<>(this);
    }

    @Override
    public String toString() {
        return "FFT= " + this.getAggValue();
    }

}
