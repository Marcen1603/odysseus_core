/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.logicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "UNNEST")
public class UnNestAO extends UnaryLogicalOp {

    /**
     * 
     */
    private static final long serialVersionUID = -5918972476973244744L;
    private static Logger LOG = LoggerFactory.getLogger(UnNestAO.class);

    SDFAttribute attribute;
    SDFAttributeList schema;

    /**
     * 
     */
    public UnNestAO() {
        super();
    }

    /**
     * @param ao
     */
    public UnNestAO(final UnNestAO ao) {
        super(ao);
        this.attribute = ao.getAttribute();
        this.schema = ao.getOutputSchema();
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator#clone()
     */
    @Override
    public AbstractLogicalOperator clone() {
        return new UnNestAO(this);
    }

    /**
     * @return The attribute for unnest
     */
    public SDFAttribute getAttribute() {
        return this.attribute;
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#getOutputSchema()
     */
    @Override
    public SDFAttributeList getOutputSchema() {
        return this.schema;
    }

    /**
     * @param attribute
     *            The attribute for unnest
     */
    @Parameter(name = "ATTRIBUTE", type = ResolvedSDFAttributeParameter.class)
    public void setAttribute(final SDFAttribute attribute) {
        UnNestAO.LOG.debug("Set UnNest attribute to {}", attribute.getAttributeName());
        this.attribute = attribute;
        SDFAttributeList schema = this.getInputSchema().clone();
        schema.getAttribute(schema.indexOf(attribute)).setDatatype(
                attribute.getDatatype().getSubType());
        this.schema = schema;
    }

}
