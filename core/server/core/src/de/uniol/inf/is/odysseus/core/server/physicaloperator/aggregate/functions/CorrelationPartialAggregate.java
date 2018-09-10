/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class CorrelationPartialAggregate<R> extends AbstractPartialAggregate<R> {

    private static final long serialVersionUID = -1700211702940005284L;
    private double sumA;
    private double sumB;
    private double squareSumA;
    private double squareSumB;
    private double crossproductSum;
    private int count;

    public CorrelationPartialAggregate(final Number a, final Number b) {
        this.add(a, b);
    }

    public CorrelationPartialAggregate(final CorrelationPartialAggregate<R> partialAggregate) {
        this.sumA = partialAggregate.sumA;
        this.sumB = partialAggregate.sumB;
        this.squareSumA = partialAggregate.squareSumA;
        this.squareSumB = partialAggregate.squareSumB;
        this.crossproductSum = partialAggregate.crossproductSum;
        this.count = partialAggregate.count;
    }

    public Double getAggValue() {
        // http://en.wikipedia.org/wiki/Correlation
        return ((this.count * this.crossproductSum) - (this.sumA * this.sumB))
                / (Math.sqrt((this.count * this.squareSumA) - Math.pow(this.sumA, 2.0)) * Math.sqrt((this.count * this.squareSumB) - Math.pow(this.sumB, 2.0)));
    }

    public void add(final Number a, final Number b) {
        if ((a != null) && (b != null)) {
            this.sumA += a.doubleValue();
            this.sumB += b.doubleValue();
            this.squareSumA += Math.pow(a.doubleValue(), 2.0);
            this.squareSumB += Math.pow(b.doubleValue(), 2.0);
            this.crossproductSum += a.doubleValue() * b.doubleValue();
            this.count++;
        }
    }

    public void add(final CorrelationPartialAggregate<?> value) {
        this.sumA += value.sumA;
        this.sumB += value.sumB;
        this.squareSumA += value.squareSumA;
        this.squareSumB += value.squareSumB;
        this.crossproductSum += value.crossproductSum;
        this.count += value.count;
    }

    @Override
    public CorrelationPartialAggregate<R> clone() {
        return new CorrelationPartialAggregate<>(this);
    }

    @Override
    public String toString() {
        return "CORR= " + this.getAggValue();
    }

    public static void main(final String[] args) {
        final CorrelationPartialAggregate<?> agg = new CorrelationPartialAggregate<>(2.1, 8.0);
        agg.add(2.5, 12.0);
        agg.add(4.0, 14.0);
        agg.add(3.6, 10.0);
        assert (agg.getAggValue() == 0.6625738822030308);
        System.out.println(agg);
    }

}
