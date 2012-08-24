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

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SumPartialAggregate<T> implements IPartialAggregate<T> {
    double sum = 0;

    public SumPartialAggregate() {
        this.sum = 0.0;
    }
    
    public SumPartialAggregate(final double value, final double probability) {
        this.sum = value * probability;
    }

    public SumPartialAggregate(final double sum) {
        this.sum = sum;
    }

    public SumPartialAggregate(final SumPartialAggregate<T> sumPartialAggregate) {
        this.sum = sumPartialAggregate.sum;
    }

    public double getSum() {
        return this.sum;
    }

    public void add(final double value, final double probability) {
        this.sum += value * probability;
    }

    @Override
    public String toString() {
        final StringBuffer ret = new StringBuffer("SumPartialAggregate (").append(this.hashCode()).append(")")
                .append(this.sum);
        return ret.toString();
    }

    @Override
    public SumPartialAggregate<T> clone() {
        return new SumPartialAggregate<T>(this);
    }
}
