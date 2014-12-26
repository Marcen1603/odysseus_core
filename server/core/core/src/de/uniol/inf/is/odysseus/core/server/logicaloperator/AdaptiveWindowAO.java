/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * Adaptive window (ADWIN) operator for change detection.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "ADWIN", doc = "Change detection window operator.", category = { LogicalOperatorCategory.PROCESSING }, deprecation = true)
public class AdaptiveWindowAO extends UnaryLogicalOp {
    /**
     * 
     */
    private static final long serialVersionUID = 587959097507970457L;
    private double delta;
    private SDFAttribute attribute;

    /**
 * 
 */
    public AdaptiveWindowAO() {
        this.delta = 0.01;
    }

    public AdaptiveWindowAO(AdaptiveWindowAO operator) {
        super(operator);
        this.delta = operator.delta;
        this.attribute = operator.attribute;
    }

    @Parameter(type = DoubleParameter.class, name = "Delta", optional = true)
    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getDelta() {
        return delta;
    }

    @Parameter(name = "ATTRIBUTE", optional = false, type = ResolvedSDFAttributeParameter.class, isList = false)
    public void setAttribute(SDFAttribute attribute) {
        this.attribute = attribute;
    }

    public int getAttributePosition() {
        SDFSchema schema = getInputSchema(0);
        SDFAttribute attribute = getAttribute();
        Objects.requireNonNull(schema);
        Objects.requireNonNull(attribute);
        return schema.indexOf(attribute);
    }

    /**
     * @return the attribute
     */
    public SDFAttribute getAttribute() {
        return this.attribute;
    }

    @Override
    public AbstractLogicalOperator clone() {
        return new AdaptiveWindowAO(this);
    }
}
