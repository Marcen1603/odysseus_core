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

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttributeList;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.ViewSchema;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class ProbabilisticViewSchema<T> extends ViewSchema<T> {
    /**
     * 
     * @param outputSchema
     *            The output schema
     * @param metaSchema
     *            The mata data schema
     * @param port
     *            The port
     */
    public ProbabilisticViewSchema(final SDFSchema outputSchema, final SDFMetaAttributeList metaSchema, final int port) {
        super(outputSchema, metaSchema, port);
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.ViewSchema#init()
     */
    @Override
    protected final void init(final List<String> preChoosenAttributes, final List<String> preGroupingAttributes) {
        int index = 0;
        for (final SDFAttribute a : this.outputSchema) {
            if (SchemaUtils.isProbabilisticAttribute(a)) {
                final IViewableAttribute attribute = new ProbabilisticViewableSDFAttribute(a, this.outputSchema.getURI(), index, this.port);
                if (ViewSchema.isAllowedDataType(attribute.getSDFDatatype())) {
                    this.viewableAttributes.add(attribute);
                }
            }
            index++;
        }
        // add all (except of currently timestamps) to the list of pre-chosen
        // attributes
        this.choosenAttributes.clear();
        for (final IViewableAttribute a : this.viewableAttributes) {
            if (this.chooseAsInitialAttribute(a.getSDFDatatype())) {
                this.choosenAttributes.add(a);
            }
        }
    }
}
