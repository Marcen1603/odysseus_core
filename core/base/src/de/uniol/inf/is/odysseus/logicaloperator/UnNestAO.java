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

    private SDFAttributeList outputSchema = new SDFAttributeList();

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
    public SDFAttributeList getOutputSchema() {
        if (outputSchema == null || recalcOutputSchemata) {
            outputSchema = new SDFAttributeList();
            for (int i = 0; i < getInputSchema().getAttributeCount(); i++) {
                if (getInputSchema().getAttribute(i).getDatatype().hasSchema()) {
                    SDFAttributeList subschema = getInputSchema().getAttribute(i).getDatatype()
                            .getSubSchema();
                    for (int j = 0; j < subschema.getAttributeCount(); j++) {
                        this.outputSchema.add(subschema.get(j));
                    }
                }
                else {
                    this.outputSchema.add(getInputSchema().get(i));
                }
            }
            recalcOutputSchemata = false;
            LOG.debug("Set output schema to: {}", outputSchema);
        }
        return outputSchema;
    }

}
