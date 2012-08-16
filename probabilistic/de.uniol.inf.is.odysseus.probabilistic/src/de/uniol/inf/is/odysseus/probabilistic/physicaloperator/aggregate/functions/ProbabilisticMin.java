/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregate.functions;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticMin<R extends Comparable<R>, W> extends AbstractAggregateFunction<R, W> {
    /**
	 * 
	 */
    private static final long serialVersionUID = -4241950598685654559L;

    protected ProbabilisticMin() {
        super("MIN");

    }

    @Override
    public IPartialAggregate<R> init(final R in) {
        return new ElementPartialAggregate<R>(in);
    }

    @Override
    public IPartialAggregate<R> merge(final IPartialAggregate<R> p, final R toMerge, final boolean createNew) {
        final ElementPartialAggregate<R> pa = null;

        return pa;
    }

    @Override
    public W evaluate(final IPartialAggregate<R> p) {
        @SuppressWarnings("unchecked")
        final ElementPartialAggregate<W> pa = (ElementPartialAggregate<W>) p;
        return pa.getElem();
    }

    private Object[] computeBins() {
        Object[] bins = new Object[] {};
        final int[] p = new int[] {};
        final int[] b = new int[] {};
        int i = 1;
        final int l = 0;
        final int e = 1;
        while (i < l) {
            final double k = Math.log(b[i]) / Math.log(1 + e);
            int q = 0;
            bins = bins;
            while (k == (Math.log(b[i]) / Math.log(1 + e))) {
                q = q + p[i];
                i++;
            }
        }
        return bins;
    }

    private double estimateMin() {
        final List p = new ArrayList();

        while (!p.isEmpty()) {

            final Object[] bins = this.computeBins();
            double w = 1;
            double U = 0;
            double V = 0;
            final double q = 0;
            for (final Object k : bins) {
                U = ((q / w) * V) + U;
                V = (1 - (q / w)) * V;
                w = w - q;
            }

        }
        final double V = 0;
        final double U = 0;
        final double e = 1;
        double min = 0;
        final double n = 0;
        final double t = (2 * Math.log(n)) / Math.log(1 + e);
        for (int i = 0; i <= t; i++) {
            double tmp = 0;
            for (int j = 0; j <= (i - 1); j++) {
                tmp *= V;
            }
            min += Math.pow((1 + e), i) * U * tmp;
        }
        return min;

    }
}
