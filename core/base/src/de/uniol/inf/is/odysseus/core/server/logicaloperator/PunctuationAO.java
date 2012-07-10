/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "PUNCTUATION")
public class PunctuationAO extends BinaryLogicalOp {

    /**
     * 
     */
    private static final long serialVersionUID = 6293636144390168094L;
    private long ratio;

    public PunctuationAO() {
        super();
    }

    public PunctuationAO(PunctuationAO ao) {
        super(ao);
        this.ratio = ao.ratio;
    }

    @Override
    public SDFSchema getOutputSchemaIntern(int pos) {
        return getInputSchema(LEFT);
    }

    @Override
    public PunctuationAO clone() {
        return new PunctuationAO(this);
    }

    /**
     * @param ratio
     *            The ratio for punctuation
     */
    @Parameter(type = LongParameter.class, name = "RATIO", optional = false)
    public void setRatio(final long ratio) {
        this.ratio = ratio;
    }

    /**
     * @return The ratio
     */
    public long getRatio() {
        return ratio;
    }
}
