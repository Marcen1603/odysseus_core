/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, deprecation = true, doc = "This Operator can be used to update the existence uncertainty information in the meta data part.", name = "Probabilistic", category = { LogicalOperatorCategory.PROBABILISTIC })
public class ProbabilisticAO extends UnaryLogicalOp {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5230887041196691992L;
    /** The attribute holding the existence probability. */
    private SDFAttribute attribute;

    /**
     * Constructs a new Probabilistic logical operator.
     */
    public ProbabilisticAO() {
        super();
    }

    /**
     * Clone constructor.
     * 
     * @param probabilisticAO
     *            The copy
     */
    public ProbabilisticAO(final ProbabilisticAO probabilisticAO) {
        super(probabilisticAO);
        this.attribute = probabilisticAO.attribute;
    }

    /**
     * Sets the attribute.
     * 
     * @param attribute
     *            The attribute
     */
    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTE", isList = false, optional = false, doc = "The name of the attribute for the existence uncertainty.")
    public final void setAttribute(final SDFAttribute attribute) {
        Objects.requireNonNull(attribute);
        this.attribute = attribute;
    }

    /**
     * Gets the attribute.
     * 
     * @return The attribute
     */
    public final SDFAttribute getAttribute() {
        return this.attribute;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
     * #clone()
     */
    @Override
    public final AbstractLogicalOperator clone() {
        return new ProbabilisticAO(this);
    }

}
