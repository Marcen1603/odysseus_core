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
public class VariancePartialAggregate<R> extends AbstractPartialAggregate<R> {

    private static final long serialVersionUID = -4774098935070401113L;
    private double diffSquareSum;
    private double mean;
    private int count;

    public VariancePartialAggregate(final Number value) {
        this.add(value);
    }

    public VariancePartialAggregate(final VariancePartialAggregate<R> partialAggregate) {
        this.mean = partialAggregate.mean;
        this.diffSquareSum = partialAggregate.diffSquareSum;
        this.count = partialAggregate.count;
    }

    public Double getAggValue() {
        return this.diffSquareSum / (this.count - 1.0);
    }

    public void add(final Number value) {
        if (value != null) {
            // Estimate online variance value using
            // Donald E. Knuth (1998). The Art of Computer Programming, volume
            // 2:
            // Seminumerical Algorithms, 3rd edn., p. 232. Boston:
            // Addison-Wesley.
            this.count++;
            final double delta = value.doubleValue() - this.mean;
            this.mean += delta / this.count;
            this.diffSquareSum += delta * (value.doubleValue() - this.mean);
        }
    }

    public void add(final VariancePartialAggregate<?> value) {
        this.mean += value.mean;
        this.diffSquareSum += value.diffSquareSum;
        this.count += value.count;
    }

    @Override
    public VariancePartialAggregate<R> clone() {
        return new VariancePartialAggregate<>(this);
    }

    @Override
    public String toString() {
        return "VAR= " + this.getAggValue();
    }

    public static void main(final String[] args) {
        final VariancePartialAggregate<?> agg = new VariancePartialAggregate<>(2.0);
        agg.add(4.0);
        agg.add(4.0);
        agg.add(4.0);
        agg.add(5.0);
        agg.add(5.0);
        agg.add(7.0);
        agg.add(9.0);
        assert (agg.getAggValue() == 4.571428571428571);
        System.out.println(agg);
    }
}
