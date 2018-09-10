/**
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(name = "ExistenceToPayload", minInputPorts = 1, maxInputPorts = 1, deprecation = true, doc = "The input object gets one new field with tuple existence.", category = { LogicalOperatorCategory.PROBABILISTIC })
public class ExistenceToPayloadAO extends UnaryLogicalOp {
    /** */
    private static final long serialVersionUID = -3582366102984336742L;

    /**
     * Default constructor.
     */
    public ExistenceToPayloadAO() {
    }

    /**
     * Clone constructor.
     * 
     * @param existenceToPayloadAO
     *            The object to copy from
     */
    public ExistenceToPayloadAO(final ExistenceToPayloadAO existenceToPayloadAO) {
        super(existenceToPayloadAO);
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
     * #getOutputSchemaIntern(int)
     */
    @Override
    public final SDFSchema getOutputSchemaIntern(final int pos) {
        Preconditions.checkPositionIndex(pos, 1);
        final SDFAttribute existence = new SDFAttribute(null, "meta_existence", SDFDatatype.DOUBLE, null, null, null);

        final List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
        String name = "";

        if (this.getInputSchema(pos) != null) {
            outputAttributes.addAll(this.getInputSchema(pos).getAttributes());
            name = this.getInputSchema(pos).getURI();
        }
        outputAttributes.add(existence);

        this.setOutputSchema(SDFSchemaFactory.createNewWithAttributes(name, outputAttributes, this.getInputSchema(0)));

        return this.getOutputSchema();
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
     * #clone()
     */
    @Override
    public final AbstractLogicalOperator clone() {
        return new ExistenceToPayloadAO(this);
    }
}
