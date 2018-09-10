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
public class CovariancePartialAggregate<R> extends AbstractPartialAggregate<R> {

    private static final long serialVersionUID = 3904441654181161018L;
    private double sumA;
    private double sumB;
    private double crossproductSum;
    private int count;

    public CovariancePartialAggregate(final Number a, final Number b) {
        this.add(a, b);
    }

    public CovariancePartialAggregate(final CovariancePartialAggregate<R> partialAggregate) {
        this.sumA = partialAggregate.sumA;
        this.sumB = partialAggregate.sumB;
        this.crossproductSum = partialAggregate.crossproductSum;
        this.count = partialAggregate.count;
    }

    public Double getAggValue() {
        return (this.crossproductSum - (this.sumA * this.sumB) / this.count) / (this.count - 1);
    }

    public void add(final Number a, final Number b) {
        if ((a != null) && (b != null)) {
            this.sumA += a.doubleValue();
            this.sumB += b.doubleValue();
            this.crossproductSum += a.doubleValue() * b.doubleValue();
            this.count++;
        }
    }

    public void add(final CovariancePartialAggregate<?> value) {
        this.sumA += value.sumA;
        this.sumB += value.sumB;
        this.crossproductSum += value.crossproductSum;
        this.count += value.count;
    }

    @Override
    public CovariancePartialAggregate<R> clone() {
        return new CovariancePartialAggregate<>(this);
    }

    @Override
    public String toString() {
        return "COV= " + this.getAggValue();
    }

    public static void main(final String[] args) {
        final CovariancePartialAggregate<?> agg = new CovariancePartialAggregate<>(2.1, 8.0);
        agg.add(2.5, 12.0);
        agg.add(4.0, 14.0);
        agg.add(3.6, 10.0);
        assert (agg.getAggValue() == 1.533333333333341);
        System.out.println(agg);
    }

}
