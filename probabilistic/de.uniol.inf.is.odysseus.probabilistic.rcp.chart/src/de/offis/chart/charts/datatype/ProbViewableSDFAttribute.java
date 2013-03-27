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
import de.uniol.inf.is.odysseus.probabilistic.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.ViewableSDFAttribute;

public class ProbViewableSDFAttribute extends ViewableSDFAttribute {

	public ProbViewableSDFAttribute(SDFAttribute attribute, String typeName,
			int index, int port) {
		super(attribute, typeName, index, port);
	}
	
	@Override
	public Object evaluate(Tuple<? extends IMetaAttribute> tuple) {
		ProbabilisticTuple<?> obj = (ProbabilisticTuple<?>)tuple;
		
		SDFProbabilisticDatatype type = (SDFProbabilisticDatatype)getSDFDatatype();
		
		if(type.isContinuous()){
			ProbabilisticContinuousDouble attr = (ProbabilisticContinuousDouble)obj.getAttribute(this.index);
			return obj.getDistribution(attr.getDistribution());
		} else if (type.isDiscrete()){			
			return (AbstractProbabilisticValue<?>)obj.getAttribute(this.index);
		}
		
		return null;
	}
}
