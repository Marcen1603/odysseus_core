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

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.relational.AbstractRelationalMergeFunction;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <K>
 * @param <T>
 */
public class ProbabilisticMergeFunction<T extends Tuple<K>, K extends IProbabilisticTimeInterval> extends AbstractRelationalMergeFunction<T, K> implements IDataMergeFunction<T, K>, Cloneable {
    /**
     * Default constructor.
     * 
     * @param resultSchemaSize
     *            The size of the result schema
     */
    public ProbabilisticMergeFunction(final int resultSchemaSize) {
        super(resultSchemaSize);
    }

    /**
     * Clone constructor.
     * 
     * @param original
     *            The object to copy from
     */
    protected ProbabilisticMergeFunction(final ProbabilisticMergeFunction<T, K> original) {
        super(original.schemaSize);
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction
     * #merge(java.lang.Object, java.lang.Object,
     * de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction,
     * de.uniol.inf.is.odysseus.core.Order)
     */
    @SuppressWarnings("unchecked")
    @Override
    public final T merge(final T left, final T right, final IMetadataMergeFunction<K> metamerge, final Order order) {
        return (T) left.merge(left, right, metamerge, order);
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction
     * #init()
     */
    @Override
    public void init() {
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.physicaloperator.relational.
     * AbstractRelationalMergeFunction#clone()
     */
    @Override
    public final ProbabilisticMergeFunction<T, K> clone() {
        return new ProbabilisticMergeFunction<T, K>(this);
    }

}
