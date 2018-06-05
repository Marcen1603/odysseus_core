package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;

/**
 * 
 * @author Henrik Surm
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "COMMAND", doc = "This operator executes commands on other operators or services.", category = { LogicalOperatorCategory.BASE })
public class CommandAO extends AbstractLogicalOperator 
{
	private static final long serialVersionUID = -578154679444642283L;

	private SDFExpression commandExpression;
	
	public CommandAO() {
		super();
	}
	
	public CommandAO(CommandAO copy) {
		super(copy);
		
		commandExpression = copy.commandExpression.clone();
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new CommandAO(this);
	}

	@Parameter(name="CommandExpression", type = NamedExpressionParameter.class, doc="Expression for the commands, e.g. an attribute or a string")
	public void setCommandExpression2(NamedExpression commandExpression) {
		this.commandExpression = commandExpression.expression;
	}
	
	public SDFExpression getCommandExpression() {
		return commandExpression;
	}

	@Override
	public boolean isValid() {
		clearErrors();
		
		if(commandExpression.getType() != SDFDatatype.COMMAND) {
			addError("CommandExpression must have return type COMMAND, not " + commandExpression.getType());
		}
		
		return getErrors().isEmpty();
	}
}
