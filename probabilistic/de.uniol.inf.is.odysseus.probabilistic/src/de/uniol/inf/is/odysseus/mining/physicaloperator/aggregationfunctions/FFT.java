/**
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.mining.physicaloperator.aggregationfunctions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AbstractListAggregation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ListPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FFT extends AbstractListAggregation<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

    /**
     * 
     */
    private static final long serialVersionUID = -6591001515663029772L;
    private final FastFourierTransformer fft;
    private final int[] restrictList;

    public FFT(int[] restrictList, boolean partialAggregateInput) {
        super("FFT", partialAggregateInput);
        this.restrictList = restrictList;
        this.fft = new FastFourierTransformer(DftNormalization.STANDARD);
    }

    @Override
    public Tuple evaluate(IPartialAggregate<Tuple<? extends IMetaAttribute>> p) {
        List<Tuple<?>> elems = ((ListPartialAggregate<Tuple<? extends IMetaAttribute>>) p).getElems();
        double[] values = new double[elems.size()];
        for (int i = 0; i < values.length; i++) {
            Tuple<?> elem = elems.get(i);
            values[i] = elem.getAttribute(restrictList[0]);
        }
        Complex[] result = fft.transform(values, TransformType.FORWARD);

        Tuple ret = new Tuple<IMetaAttribute>(2, false);
        List<Double> realNumbers = new ArrayList<Double>();
        List<Double> imaginaryNumbers = new ArrayList<Double>();

        for (Complex complex : result) {
            realNumbers.add(complex.getReal());
            imaginaryNumbers.add(complex.getImaginary());
        }
        // FIXME Is the schema extended to two attributes?
        // Maybe use Complex as an additional datatype
        ret.setAttribute(0, realNumbers);
        ret.setAttribute(1, imaginaryNumbers);
        return ret;
    }

    @Override
    public IPartialAggregate<Tuple<? extends IMetaAttribute>> merge(IPartialAggregate<Tuple<? extends IMetaAttribute>> p, Tuple<? extends IMetaAttribute> toMerge, boolean createNew) {
        return ((ListPartialAggregate) p).addElem(toMerge.restrict(restrictList, true));
    }

}
