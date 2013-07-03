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
package de.offis.chart.charts.datatype;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.ViewableSDFAttribute;

public class ProbabilisticViewableSDFAttribute extends ViewableSDFAttribute {

    public ProbabilisticViewableSDFAttribute(final SDFAttribute attribute, final String typeName, final int index, final int port) {
        super(attribute, typeName, index, port);
    }

    @Override
    public Object evaluate(final Tuple<? extends IMetaAttribute> tuple) {
        final ProbabilisticTuple<?> obj = (ProbabilisticTuple<?>) tuple;

        final SDFProbabilisticDatatype type = (SDFProbabilisticDatatype) this.getSDFDatatype();

        if (type.isContinuous()) {
            final ProbabilisticContinuousDouble attr = (ProbabilisticContinuousDouble) obj.getAttribute(this.index);
            return obj.getDistribution(attr.getDistribution());
        } else if (type.isDiscrete()) {
            return obj.getAttribute(this.index);
        }

        return null;
    }
    
    public int getIndex() {
        return index;
    }
}
