/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, deprecation = true, doc = "Store quality information in the metadata.", name = "QualityIndicator", category = { LogicalOperatorCategory.ONTOLOGY })
public class QualityIndicatorAO extends UnaryLogicalOp {
    /**
     * 
     */
    private static final long serialVersionUID = 3356890965532635493L;
    private SDFAttribute frequency;
    private SDFAttribute completeness;
    private SDFAttribute consistency;

    /**
     * Class constructor.
     * 
     */
    public QualityIndicatorAO() {
        super();
    }

    /**
     * Clone constructor.
     * 
     * @param ao
     *            The instance to clone from
     */
    public QualityIndicatorAO(final QualityIndicatorAO ao) {
        super(ao);
        Objects.requireNonNull(ao);
        this.setCompleteness(ao.completeness);
        this.setConsistency(ao.consistency);
        this.setFrequency(ao.frequency);

    }

    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "COMPLETENESS", isList = false, optional = true)
    public void setCompleteness(final SDFAttribute completeness) {
        this.completeness = completeness;
    }

    @GetParameter(name = "COMPLETENESS")
    public SDFAttribute getCompleteness() {
        return this.completeness;
    }

    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "CONSISTENCY", isList = false, optional = true)
    public void setConsistency(final SDFAttribute consistency) {
        this.consistency = consistency;
    }

    @GetParameter(name = "CONSISTENCY")
    public SDFAttribute getConsistency() {
        return this.consistency;
    }

    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "FREQUENCY", isList = false, optional = true)
    public void setFrequency(final SDFAttribute frequency) {
        Objects.requireNonNull(frequency);
        this.frequency = frequency;
    }

    @GetParameter(name = "FREQUENCY")
    public SDFAttribute getFrequency() {
        return this.frequency;
    }

    public boolean hasCompleteness() {
        return this.completeness != null;
    }

    public boolean hasConsistency() {
        return this.consistency != null;
    }

    public boolean hasFrequency() {
        return this.frequency != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLogicalOperator clone() {
        return new QualityIndicatorAO(this);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        super.initialize();
        Objects.requireNonNull(this.frequency);
    }

}
