/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "GENERATOR", doc = "Generates missing values in a stream", category = { LogicalOperatorCategory.ADVANCED })
public class GeneratorAO extends UnaryLogicalOp implements IHasPredicate{

    /**
     * 
     */
    private static final long serialVersionUID = 101908321734981396L;
    private List<NamedExpression> namedExpressions;
    private List<SDFExpression> expressions;
    private boolean allowNull = false;
    private List<SDFAttribute> groupingAttributes;
    private int frequency;
    private boolean multi = false;
	private IPredicate<?> predicate;

    public GeneratorAO() {
        super();
    }

    public GeneratorAO(final GeneratorAO ao) {
        super(ao);
        this.setExpressions(ao.namedExpressions);
        this.allowNull = ao.allowNull;
        this.groupingAttributes = ao.groupingAttributes;
        this.frequency = ao.frequency;
        this.multi = ao.multi;
        if (ao.predicate != null){
        	predicate = ao.predicate.clone();
        }
    }

    @SuppressWarnings("rawtypes")
    @Parameter(type = PredicateParameter.class)
    public void setPredicate(final IPredicate predicate) {
        this.predicate = predicate;
    }
    
    @Override
    public IPredicate<?> getPredicate() {
    	return predicate;
    }

    @Parameter(type = BooleanParameter.class, optional = true)
    public void setAllowNullInOutput(final boolean allowNull) {
        this.allowNull = allowNull;
    }

    /**
     * @return
     */
    public boolean isAllowNullInOutput() {
        return this.allowNull;
    }

    /**
     * @param multi
     *            the multi to set
     */
    @Parameter(type = BooleanParameter.class, optional = true)
    public void setMulti(final boolean multi) {
        this.multi = multi;
    }

    /**
     * @return the multi
     */
    public boolean isMulti() {
        return this.multi;
    }

    @Parameter(type = IntegerParameter.class, optional = false)
    public void setFrequency(final int frequency) {
        this.frequency = frequency;
    }

    /**
     * @return
     */
    public int getFrequency() {
        return this.frequency;
    }

    @Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
    public void setGroupingAttributes(final List<SDFAttribute> attributes) {
        this.groupingAttributes = attributes;
    }

    public List<SDFAttribute> getGroupingAttributes() {
        return this.groupingAttributes;
    }

    @Parameter(type = NamedExpressionParameter.class, isList = true)
    public void setExpressions(final List<NamedExpression> namedExpressions) {
        this.namedExpressions = namedExpressions;
        this.expressions = new ArrayList<>();
        for (final NamedExpression e : namedExpressions) {
            this.expressions.add(e.expression);
        }
    }

    public List<SDFExpression> getExpressions() {
        return Collections.unmodifiableList(this.expressions);
    }

    @Override
    public SDFSchema getOutputSchemaIntern(final int pos) {
        return this.getInputSchema();
    }

    @Override
    public GeneratorAO clone() {
        return new GeneratorAO(this);
    }

}
