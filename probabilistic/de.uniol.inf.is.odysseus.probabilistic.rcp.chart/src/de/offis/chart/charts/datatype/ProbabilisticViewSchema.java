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

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttributeList;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.ViewSchema;

public class ProbabilisticViewSchema<T> extends ViewSchema<T> {

	public ProbabilisticViewSchema(final SDFSchema outputSchema,
			final SDFMetaAttributeList metaSchema, final int port) {
		super(outputSchema, metaSchema, port);
	}

	private boolean isProbalisticDatatype(final SDFDatatype type) {
		return type.getClass().equals(SDFProbabilisticDatatype.class);
	}

	@Override
	protected void init() {
		int index = 0;
		for (final SDFAttribute a : this.outputSchema) {
			if (this.isProbalisticDatatype(a.getDatatype())) {
				final IViewableAttribute attribute = new ProbabilisticViewableSDFAttribute(
						a, this.outputSchema.getURI(), index, this.port);
				if (ViewSchema.isAllowedDataType(attribute.getSDFDatatype())) {
					this.viewableAttributes.add(attribute);
				}
			}
			index++;
		}
		// add all (except of currently timestamps) to the list of pre-chosen
		// attributes
		this.choosenAttributes = new ArrayList<IViewableAttribute>();
		for (final IViewableAttribute a : this.viewableAttributes) {
			if (this.chooseAsInitialAttribute(a.getSDFDatatype())) {
				this.choosenAttributes.add(a);
			}
		}

		// for (final SDFMetaAttribute m : this.metadataSchema) {
		// for (final Method method : m.getMetaAttributeClass().getMethods()) {
		// if (!method.getName().endsWith("hashCode")) {
		// final IViewableAttribute attribute = new ViewableMetaAttribute(m,
		// method, this.port);
		// if ((method.getParameterTypes().length == 0) &&
		// ViewSchema.isAllowedDataType(attribute.getSDFDatatype())) {
		// this.viewableAttributes.add(new ViewableMetaAttribute(m, method,
		// this.port));
		// }
		// }
		// }
		// }
	}
}
