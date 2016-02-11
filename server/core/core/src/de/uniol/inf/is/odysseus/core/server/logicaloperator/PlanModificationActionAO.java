package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;

@LogicalOperator(name="PlanModificationAction", maxInputPorts=1, minInputPorts=1, doc="Executes plan modifications based on receiving tuple data", category = { LogicalOperatorCategory.PLAN})
public class PlanModificationActionAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -578154679444642283L;

	private SDFExpression commandExpression;
	private SDFExpression queryIDExpression;
	
	public PlanModificationActionAO() {
		super();
	}
	
	public PlanModificationActionAO( PlanModificationActionAO copy ) {
		super(copy);
		
		commandExpression = copy.commandExpression.clone();
		queryIDExpression = copy.queryIDExpression.clone();
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new PlanModificationActionAO(this);
	}

	@Parameter(name="CommandExpression", type = NamedExpressionParameter.class, doc="Expression for the plan modification commands, e.g. an attribute or a string")
	public void setCommandExpression2(NamedExpression commandExpression) {
		this.commandExpression = commandExpression.expression;
	}
	
	public SDFExpression getCommandExpression() {
		return commandExpression;
	}

	@Parameter(name="QueryIDExpression", type = NamedExpressionParameter.class, doc="Expression to calculate the query id to execute the commands on")
	public void setQueryIDExpression2( NamedExpression attribute ) {
		queryIDExpression = attribute.expression;
	}
	
	public SDFExpression getQueryIDExpression() {
		return queryIDExpression;
	}
	
	@Override
	public boolean isValid() {
		clearErrors();
		
		if( !isStringExpression(commandExpression)) {
			addError("CommandExpression must have return type STRING, not " + commandExpression.getType());
		}
		
		if( !isNumericExpression(queryIDExpression)) {
			addError("QueryIDAttribute must be of numeric type (e.g., INTEGER), not " + queryIDExpression.getType().getQualName());
		}
		
		return getErrors().isEmpty();
	}

	private static boolean isStringExpression(SDFExpression expression) {
		return expression.getType().isString();
	}
	
	private static boolean isNumericExpression(SDFExpression attribute) {
		return attribute.getType().isNumeric();
	}
}
