/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.ontology.metadata;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.metadata.AbstractMetadataUpdater;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class QualityFactory<M extends IQuality, T extends Tuple<M>> extends AbstractMetadataUpdater<M, T> {

    /** The position of the attribute holding the completeness. */
    private final int completenessPos;
    /** The position of the attribute holding the consistency. */
    private final int consistencyPos;
    /** The position of the attribute holding the frequency. */
    private final int frequencyPos;

    /**
     *
     * Class constructor.
     *
     * @param completenessPos
     * @param consistencyPos
     */
    public QualityFactory(final int completenessPos, final int consistencyPos) {
        this.completenessPos = completenessPos;
        this.consistencyPos = consistencyPos;
        this.frequencyPos = -1;
    }

    /**
     *
     * Class constructor.
     *
     * @param completenessPos
     * @param consistencyPos
     */
    public QualityFactory(final int completenessPos, final int consistencyPos, final int frequencyPos) {
        this.completenessPos = completenessPos;
        this.consistencyPos = consistencyPos;
        this.frequencyPos = frequencyPos;
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
        if (this.frequencyPos > 0) {
            metadata.setFrequency(((Number) inElem.getAttribute(this.frequencyPos)).doubleValue());
        }
    }

}
