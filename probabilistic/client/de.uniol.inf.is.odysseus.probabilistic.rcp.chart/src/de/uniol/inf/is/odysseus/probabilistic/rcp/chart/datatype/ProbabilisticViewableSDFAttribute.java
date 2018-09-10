/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package de.uniol.inf.is.odysseus.probabilistic.rcp.chart.datatype;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.ViewableSDFAttribute;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticViewableSDFAttribute extends ViewableSDFAttribute {
    /**
     * 
     * @param attribute
     *            The SDF attribute
     * @param typeName
     *            The type name
     * @param index
     *            The index
     * @param port
     *            The port
     */
    public ProbabilisticViewableSDFAttribute(final SDFAttribute attribute, final String typeName, final int index, final int port) {
        super(attribute, typeName, index, port);
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.ViewableSDFAttribute
     * #evaluate(de.uniol.inf.is.odysseus.core.collection.Tuple)
     */
    @Override
    public final Object evaluate(final Tuple<? extends IMetaAttribute> tuple) {
        final ProbabilisticTuple<?> obj = (ProbabilisticTuple<?>) tuple;
        final ProbabilisticDouble attr = (ProbabilisticDouble) obj.getAttribute(this.index);
        return obj.getDistribution(attr.getDistribution());
    }

    /**
     * Gets the value of the index property.
     * 
     * @return The value of the index property
     */
    public final int getIndex() {
        return this.index;
    }
}
