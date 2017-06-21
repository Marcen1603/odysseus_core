package de.uniol.inf.is.odysseus.spatial.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;

@LogicalOperator(name = "CreateUpdateExpressionsPunctuation", maxInputPorts = 1, minInputPorts = 1, doc = "Creates a punctuation with which the expressions can be updated if the receiving operator support it.", category = {
		LogicalOperatorCategory.PROCESSING })
public class CreateUpdateExpressionsPunctuationAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 6224831223141503939L;
	private List<NamedExpression> namedExpressions;
	private List<SDFExpression> expressions;

	public CreateUpdateExpressionsPunctuationAO() {

	}

	public CreateUpdateExpressionsPunctuationAO(CreateUpdateExpressionsPunctuationAO ao) {
		this.namedExpressions = ao.getNamedExpressions();
		this.expressions = ao.getExpressions();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new CreateUpdateExpressionsPunctuationAO(this);
	}

	@Parameter(type = NamedExpressionParameter.class, name = "EXPRESSIONS", aliasname = "kvExpressions", isList = true, optional = false, doc = "A list of expressions.")
	public void setNamedExpressions(List<NamedExpression> namedExpressions) {
		this.namedExpressions = namedExpressions;
		expressions = new ArrayList<>();
		if (namedExpressions != null) {
			for (NamedExpression e : namedExpressions) {
				expressions.add(e.expression);
			}
		}
		setOutputSchema(null);
	}

	public List<NamedExpression> getNamedExpressions() {
		return this.namedExpressions;
	}

	public List<SDFExpression> getExpressions() {
		return expressions;
	}
}
