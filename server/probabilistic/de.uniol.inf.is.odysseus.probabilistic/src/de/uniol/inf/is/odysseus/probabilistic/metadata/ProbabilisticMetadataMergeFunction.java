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

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;

/**
 * Merge function for probabilistic data streams.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class ProbabilisticMetadataMergeFunction implements IInlineMetadataMergeFunction<IProbabilistic>, Cloneable {
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final void mergeInto(final IProbabilistic result, final IProbabilistic inLeft, final IProbabilistic inRight) {
        result.setExistence(inLeft.getExistence() * inRight.getExistence());
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final ProbabilisticMetadataMergeFunction clone() {
        return new ProbabilisticMetadataMergeFunction();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final Class<? extends IMetaAttribute> getMetadataType() {
        return IProbabilistic.class;
    }

}
