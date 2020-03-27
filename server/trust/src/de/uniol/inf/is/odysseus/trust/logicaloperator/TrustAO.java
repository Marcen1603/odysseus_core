package de.uniol.inf.is.odysseus.trust.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "Trust", doc = "This Operator can be used to update the trust information in the meta data part. Be careful because this may lead undefined semantics", category = {
		LogicalOperatorCategory.ADVANCED })
public class TrustAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -4952583300216224870L;

	private SDFExpression trustExpression;

	public TrustAO() {
	}

	public TrustAO(TrustAO other) {
		super(other);
		trustExpression = other.trustExpression != null ? other.trustExpression.clone() : null;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TrustAO(this);
	}

	@Parameter(type = NamedExpressionParameter.class, name = "TRUST", optional = false, doc = "Expression to calculate the trust. Could be an attribute, too.")
	public void setTrustExpression2(NamedExpression expression) {
		trustExpression = expression.expression;
	}

	public SDFExpression getTrustExpression() {
		return trustExpression;
	}

}