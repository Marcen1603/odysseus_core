/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
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
package cc.kuka.odysseus.ontology.logicaloperator;

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
 * @version $Id$
 *
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, doc = "Store quality information in the metadata.", url = "https://kuka.cc/software/odysseus/ontology", name = "QualityIndicator", category = { LogicalOperatorCategory.ONTOLOGY })
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
     * @param clone
     *            The instance to clone from
     */
    public QualityIndicatorAO(final QualityIndicatorAO clone) {
        super(clone);
        Objects.requireNonNull(clone);
        this.setCompleteness(clone.completeness);
        this.setConsistency(clone.consistency);
        this.setFrequency(clone.frequency);

    }

    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "COMPLETENESS", isList = false, optional = true, doc = "The attribute holding the completeness information")
    public void setCompleteness(final SDFAttribute completeness) {
        this.completeness = completeness;
    }

    @GetParameter(name = "COMPLETENESS")
    public SDFAttribute getCompleteness() {
        return this.completeness;
    }

    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "CONSISTENCY", isList = false, optional = true, doc = "The attribute holding the consistency information")
    public void setConsistency(final SDFAttribute consistency) {
        this.consistency = consistency;
    }

    @GetParameter(name = "CONSISTENCY")
    public SDFAttribute getConsistency() {
        return this.consistency;
    }

    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "FREQUENCY", isList = false, optional = true, doc = "The attribute holding the frequency information")
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
