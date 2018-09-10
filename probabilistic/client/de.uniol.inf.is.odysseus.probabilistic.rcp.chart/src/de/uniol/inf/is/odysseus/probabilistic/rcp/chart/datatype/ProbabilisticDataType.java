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

import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.AbstractViewableDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticDataType extends AbstractViewableDatatype<Object> {
    /**
     * Default constructor registering probabilistic data types.
     */
    public ProbabilisticDataType() {
        super.addProvidedSDFDatatype(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE);
        super.addProvidedClass(ProbabilisticDouble.class);
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableDatatype
     * #convertToValue(java.lang.Object)
     */
    @Override
    public final Object convertToValue(final Object value) {
        return value;
    }

}
