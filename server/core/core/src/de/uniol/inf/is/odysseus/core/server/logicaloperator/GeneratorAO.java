/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "GENERATOR", doc = "Generats missing values in a stream", category = { LogicalOperatorCategory.ADVANCED })
public class GeneratorAO extends BinaryLogicalOp {

    /**
     * 
     */
    private static final long serialVersionUID = 101908321734981396L;
    private List<NamedExpressionItem> namedExpressions;
    private List<SDFExpression> expressions;
    private boolean allowNull = false;
    private List<SDFAttribute> groupingAttributes;
    private int frequency;

    public GeneratorAO() {
        super();
    }

    public GeneratorAO(GeneratorAO ao) {
        super(ao);
        this.setExpressions(ao.namedExpressions);
        allowNull = ao.allowNull;
        this.groupingAttributes = ao.groupingAttributes;
        this.frequency = ao.frequency;
    }

    @Parameter(type = BooleanParameter.class, optional = true)
    public void setAllowNullInOutput(boolean allowNull) {
        this.allowNull = allowNull;
    }

    /**
     * @return
     */
    public boolean isAllowNullInOutput() {
        return allowNull;
    }

    @Parameter(type = IntegerParameter.class, optional = false)
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    /**
     * @return
     */
    public int getFrequency() {
        return frequency;
    }

    @Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
    public void setGroupingAttributes(List<SDFAttribute> attributes) {
        this.groupingAttributes = attributes;
    }

    public List<SDFAttribute> getGroupingAttributes() {
        return groupingAttributes;
    }

    @Parameter(type = SDFExpressionParameter.class, isList = true)
    public void setExpressions(List<NamedExpressionItem> namedExpressions) {
        this.namedExpressions = namedExpressions;
        expressions = new ArrayList<>();
        for (NamedExpressionItem e : namedExpressions) {
            expressions.add(e.expression);
        }
    }

    public List<SDFExpression> getExpressions() {
        return Collections.unmodifiableList(expressions);
    }

    @Override
    public SDFSchema getOutputSchemaIntern(int pos) {
        return getInputSchema(1);
    }

    @Override
    public GeneratorAO clone() {
        return new GeneratorAO(this);
    }

}
