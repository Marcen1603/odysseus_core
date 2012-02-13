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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

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
    private SDFAttribute attribute;
    private SDFSchema outputSchema = null;

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
        this.outputSchema = ao.getOutputSchema();
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator#clone()
     */
    @Override
    public AbstractLogicalOperator clone() {
        return new UnNestAO(this);
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#getOutputSchema()
     */
    @Override
    public SDFSchema getOutputSchema() {
        if (outputSchema == null || recalcOutputSchemata) {
        	List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
            for (int i = 0; i < getInputSchema().size(); i++) {
                if ((getInputSchema().getAttribute(i).equals(attribute))
                        && (getInputSchema().getAttribute(i).getDatatype().hasSchema())) {
                    SDFSchema subschema = getInputSchema().getAttribute(i).getDatatype()
                            .getSchema();
                    for (int j = 0; j < subschema.size(); j++) {
                        attrs.add(subschema.get(j));
                    }
                }
                else {
                    attrs.add(getInputSchema().get(i));
                }
            }
            recalcOutputSchemata = false;
            outputSchema = new SDFSchema("UNNEST", attrs);
            LOG.debug("Set output schema to: {}", outputSchema);
        }
        return outputSchema;
    }

    /**
     * @param attribute
     *            The attribute for unnest
     */
    @Parameter(name = "ATTRIBUTE", type = ResolvedSDFAttributeParameter.class)
    public void setAttribute(final SDFAttribute attribute) {
        UnNestAO.LOG.debug("Set UnNest attribute to {}", attribute.getAttributeName());
        this.attribute = attribute;
    }

    /**
     * @return The attribute for unnest
     */
    public SDFAttribute getAttribute() {
        return this.attribute;
    }
    
    public int getAttributePosition() {
        return this.getInputSchema().indexOf(getAttribute());
    }
}
