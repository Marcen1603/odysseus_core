/*
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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "EM")
public class EMAO extends UnaryLogicalOp {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4183569304131228484L;
    private List<SDFAttribute> attributes;

    public EMAO() {
        super();
    }

    public EMAO(EMAO emAO) {
        super(emAO);
        this.attributes = new ArrayList<SDFAttribute>(emAO.attributes);
    }

    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", isList = true, optional = false)
    public void setAttributes(final List<SDFAttribute> attributes) {
        this.attributes = attributes;
    }

    @GetParameter(name = "ATTRIBUTES")
    public List<SDFAttribute> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new ArrayList<SDFAttribute>();
        }
        return this.attributes;
    }

    public int[] determineAttributesList() {
        return calcAttributeList(getInputSchema(), getAttributes());
    }

    public static int[] calcAttributeList(SDFSchema in, List<SDFAttribute> attributes) {
        int[] ret = new int[attributes.size()];
        int i = 0;
        for (SDFAttribute attr : attributes) {
            if (!in.contains(attr)) {
                throw new IllegalArgumentException("no such attribute: " + attr);
            } else {
                ret[i] = in.indexOf(attr);
                i++;
            }
        }
        return ret;
    }

    @Override
    public AbstractLogicalOperator clone() {
        return new EMAO(this);
    }
}
