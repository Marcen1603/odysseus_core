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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.metadata.AbstractMetadataUpdater;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class QualityFactory<M extends IQuality, T extends Tuple<M>> extends AbstractMetadataUpdater<M, T> {

    /** The position of the attribute holding the completeness. */
    private final int completenessPos;
    /** The position of the attribute holding the consistency. */
    private final int consistencyPos;

    /**
     * Creates a new {@link QualityTimeIntervalFactory}.
     * 
     * @param existenceProbabilityPos
     *            The position of the attribute
     */
    public QualityFactory(final int completenessPos, final int consistencyPos) {
        this.completenessPos = completenessPos;
        this.consistencyPos = consistencyPos;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void updateMetadata(final T inElem) {
        final IQuality metadata = inElem.getMetadata();
        if (this.completenessPos > 0) {
            metadata.setCompleteness(((Number) inElem.getAttribute(this.completenessPos)).doubleValue());
        }
        if (this.consistencyPos > 0) {
            metadata.setConsistency(((Number) inElem.getAttribute(this.consistencyPos)).doubleValue());
        }
    }

}
