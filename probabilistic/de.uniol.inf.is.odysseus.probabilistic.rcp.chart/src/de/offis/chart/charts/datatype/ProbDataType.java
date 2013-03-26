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

import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.AbstractViewableDatatype;

public class ProbDataType extends AbstractViewableDatatype<Object> {

	public ProbDataType() {		
		super.addProvidedSDFDatatype(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE);
		super.addProvidedSDFDatatype(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE);
		super.addProvidedClass(ProbabilisticContinuousDouble.class);
		super.addProvidedClass(ProbabilisticDouble.class);
	}
	
	@Override
	public Object convertToValue(Object value) {
		return value;
	}

}
