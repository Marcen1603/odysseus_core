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
package de.uniol.inf.is.odysseus.probabilistic.metadata;

import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.server.intervalapproach.IDummyDataCreationFunction;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class DefaultProbabilisticTIDummyDataCreation implements IDummyDataCreationFunction<IProbabilisticTimeInterval, ProbabilisticTuple<IProbabilisticTimeInterval>>, Cloneable {
    /**
     * Default constructor.
     */
    public DefaultProbabilisticTIDummyDataCreation() {
    }

    /**
     * Clone constructor.
     * 
     * @param defaultTIDummyDataCreation
     *            The object to copy from
     */
    public DefaultProbabilisticTIDummyDataCreation(final DefaultProbabilisticTIDummyDataCreation defaultTIDummyDataCreation) {
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.server.intervalapproach.IDummyDataCreationFunction
     * #createMetadata(de.uniol.inf.is.odysseus.core.metadata.IStreamObject)
     */
    @Override
    public final ProbabilisticTuple<IProbabilisticTimeInterval> createMetadata(final ProbabilisticTuple<IProbabilisticTimeInterval> source) {
        return source.clone();
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.server.intervalapproach.IDummyDataCreationFunction
     * #hasMetadata(de.uniol.inf.is.odysseus.core.metadata.IStreamObject)
     */
    @Override
    public final boolean hasMetadata(final ProbabilisticTuple<IProbabilisticTimeInterval> source) {
        return true;
    }

    /*
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public final DefaultProbabilisticTIDummyDataCreation clone() {
        return new DefaultProbabilisticTIDummyDataCreation(this);
    }

}
