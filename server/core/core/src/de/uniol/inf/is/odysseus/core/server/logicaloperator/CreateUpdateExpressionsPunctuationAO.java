package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;

@LogicalOperator(name = "CreateUpdateExpressionsPunctuation", maxInputPorts = 1, minInputPorts = 1, doc = "Creates a punctuation with which the expressions can be updated if the receiving operator support it.", category = {
		LogicalOperatorCategory.PROCESSING })
public class CreateUpdateExpressionsPunctuationAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 6224831223141503939L;
	private List<Option> namedStrings;

	public CreateUpdateExpressionsPunctuationAO() {

	}

	public CreateUpdateExpressionsPunctuationAO(CreateUpdateExpressionsPunctuationAO ao) {
		this.namedStrings = ao.getNamedStrings();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new CreateUpdateExpressionsPunctuationAO(this);
	}

	@Parameter(type = OptionParameter.class, name = "ExpressionTemplates", isList = true, optional = false, doc = "A list of expressionTemplates. A template can include an attribute name in <>-brackets (<attribute>) which is replaced by the value of the attribute.")
	public void setNamedStrings(List<Option> namedStrings) {
		this.namedStrings = namedStrings;
	}

	public List<Option> getNamedStrings() {
		return this.namedStrings;
	}
}
