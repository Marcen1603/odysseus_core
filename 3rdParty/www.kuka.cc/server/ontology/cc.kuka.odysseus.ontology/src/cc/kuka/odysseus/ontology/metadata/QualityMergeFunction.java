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

import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class QualityMergeFunction implements IInlineMetadataMergeFunction<IQuality> {
    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final void mergeInto(final IQuality result, final IQuality inLeft, final IQuality inRight) {
        result.setCompleteness(inLeft.getCompleteness() * inRight.getCompleteness());
        result.setConsistency(inLeft.getConsistency() * inRight.getConsistency());
        result.setConsistency(1.0 / ((1.0 / inLeft.getFrequency() + 1.0 / inRight.getFrequency()) / 2.0));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final QualityMergeFunction clone() {
        return new QualityMergeFunction();
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
