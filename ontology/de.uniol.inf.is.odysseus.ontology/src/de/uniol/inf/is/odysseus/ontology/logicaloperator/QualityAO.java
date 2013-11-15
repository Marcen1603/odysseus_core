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
package de.uniol.inf.is.odysseus.ontology.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, doc = "Append quality information to the incoming stream object.", name = "Quality", category = { LogicalOperatorCategory.ONTOLOGY })
public class QualityAO extends UnaryLogicalOp {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7153504084002972374L;
    private List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
    private List<String> properties = new ArrayList<String>();

    /**
     * Class constructor.
     * 
     */
    public QualityAO() {
        super();
    }

    /**
     * Clone constructor.
     * 
     * @param qualityAO
     *            The instance to clone from
     */
    public QualityAO(final QualityAO qualityAO) {
        super(qualityAO);
        this.attributes = new ArrayList<SDFAttribute>(qualityAO.attributes);
        this.properties = new ArrayList<String>(qualityAO.properties);
    }

    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", optional = false, isList = true)
    public void setAttributes(List<SDFAttribute> attributes) {
        this.attributes = attributes;
    }

    @GetParameter(name = "ATTRIBUTES")
    public List<SDFAttribute> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new ArrayList<SDFAttribute>();
        }
        return this.attributes;
    }

    @Parameter(type = StringParameter.class, name = "PROPERTIES", optional = false, isList = true)
    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    @GetParameter(name = "PROPERTIES")
    public List<String> getProperties() {
        if (this.properties == null) {
            this.properties = new ArrayList<String>();
        }
        return this.properties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLogicalOperator clone() {
        return new QualityAO(this);
    }

}
