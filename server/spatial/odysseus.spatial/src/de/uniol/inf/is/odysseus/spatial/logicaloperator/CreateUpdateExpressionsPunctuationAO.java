package de.uniol.inf.is.odysseus.spatial.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedString;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedStringParameter;

@LogicalOperator(name = "CreateUpdateExpressionsPunctuation", maxInputPorts = 1, minInputPorts = 1, doc = "Creates a punctuation with which the expressions can be updated if the receiving operator support it.", category = {
		LogicalOperatorCategory.PROCESSING })
public class CreateUpdateExpressionsPunctuationAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 6224831223141503939L;
	private List<NamedString> namedStrings;
//	private List<SDFExpression> expressions;

	public CreateUpdateExpressionsPunctuationAO() {

	}

	public CreateUpdateExpressionsPunctuationAO(CreateUpdateExpressionsPunctuationAO ao) {
		this.namedStrings = ao.getNamedStrings();
//		this.expressions = ao.getExpressions();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new CreateUpdateExpressionsPunctuationAO(this);
	}

	@Parameter(type = NamedStringParameter.class, name = "ExpressionTemplates", isList = true, optional = false, doc = "A list of expressionTemplates.")
	public void setNamedStrings(List<NamedString> namedStrings) {
		this.namedStrings = namedStrings;
	}

	public List<NamedString> getNamedStrings() {
		return this.namedStrings;
	}
}
