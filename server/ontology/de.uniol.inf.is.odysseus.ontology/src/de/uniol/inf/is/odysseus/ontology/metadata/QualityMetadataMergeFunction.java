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
package de.uniol.inf.is.odysseus.ontology.metadata;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.IInlineMetadataMergeFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class QualityMetadataMergeFunction implements IInlineMetadataMergeFunction<IQuality>, Cloneable {
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final void mergeInto(final IQuality result, final IQuality inLeft, final IQuality inRight) {
        result.setCompleteness(inLeft.getCompleteness() * inRight.getCompleteness());
        result.setConsistency(inLeft.getConsistency() * inRight.getConsistency());
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final QualityMetadataMergeFunction clone() {
        return new QualityMetadataMergeFunction();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final Class<? extends IMetaAttribute> getMetadataType() {
        return IQuality.class;
    }

}
