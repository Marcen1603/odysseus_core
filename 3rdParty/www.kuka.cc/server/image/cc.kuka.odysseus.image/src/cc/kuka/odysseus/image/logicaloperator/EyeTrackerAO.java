/*******************************************************************************
 * Copyright (C) 2015  Christian Kuka <christian@kuka.cc>
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
package cc.kuka.odysseus.image.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import cc.kuka.odysseus.image.common.sdf.schema.SDFImageDatatype;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
@LogicalOperator(maxInputPorts = 2, minInputPorts = 1, name = "EYETRACKER", doc = "This operator tracks pupiles in an image and reports the relative position.", category = { LogicalOperatorCategory.ADVANCED })
public class EyeTrackerAO extends BinaryLogicalOp {
    /**
     *
     */
    private static final long serialVersionUID = 739629101238514514L;
    private SDFAttribute attribute;

    /**
     * Class constructor.
     *
     */
    public EyeTrackerAO() {
    }

    /**
     * Class constructor.
     *
     */
    public EyeTrackerAO(final EyeTrackerAO operator) {
        this.attribute = operator.attribute;
    }

    @Parameter(name = "ATTRIBUTE", optional = false, type = ResolvedSDFAttributeParameter.class, isList = false, doc = "Attribute holding the image")
    public void setAttribute(final SDFAttribute attribute) {
        this.attribute = attribute;
    }

    /**
     * @return the attribute
     */
    @GetParameter(name = "ATTRIBUTE")
    public SDFAttribute getAttribute() {
        return this.attribute;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EyeTrackerAO clone() {
        return new EyeTrackerAO(this);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public SDFSchema getOutputSchemaIntern(final int pos) {
        this.calcOutputSchema();
        return this.getOutputSchema();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        super.initialize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        this.addError(this.attribute == null, "No attribute selected, select an attribute of type " + SDFImageDatatype.IMAGE);
        this.addError(!SDFImageDatatype.IMAGE.compatibleTo(this.attribute.getDatatype()), "Invalid attribute datatype, attribute must be of type " + SDFImageDatatype.IMAGE);
        if (this.getNumberOfInputs() == 2) {
            this.addError(!this.getInputSchema(BinaryLogicalOp.LEFT).compatibleTo(this.getInputSchema(BinaryLogicalOp.RIGHT)), "Input schemas not compatible, both schemas must be equal");
        }
        return !this.hasErrors();
    }

    private void calcOutputSchema() {
        final List<SDFAttribute> outputSchemaAttributes = new ArrayList<>();
        for (final SDFAttribute attr : this.getInputSchema(BinaryLogicalOp.LEFT)) {
            if (attr.equals(this.attribute)) {
                outputSchemaAttributes.add(new SDFAttribute(attr.getURIWithoutQualName(), attr.getAttributeName(), SDFDatatype.LIST_DOUBLE, attr.getUnit(), attr.getDtConstraints()));
            }
            else {
                outputSchemaAttributes.add(attr.clone());
            }
        }
        this.setOutputSchema(SDFSchemaFactory.createNewSchema(this.getInputSchema(BinaryLogicalOp.LEFT).getURI(), this.getInputSchema(BinaryLogicalOp.LEFT).getType(), outputSchemaAttributes));
    }
}
