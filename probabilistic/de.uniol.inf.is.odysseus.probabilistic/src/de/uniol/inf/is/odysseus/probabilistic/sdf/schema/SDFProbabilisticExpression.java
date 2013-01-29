package de.uniol.inf.is.odysseus.probabilistic.sdf.schema;

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IExpressionParser;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpressionParseException;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;

public class SDFProbabilisticExpression extends SDFExpression {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2862460974715363031L;
	private NormalDistributionMixture[] distributions;

	public SDFProbabilisticExpression(IExpression<?> expression,
			IAttributeResolver attributeResolver,
			IExpressionParser expressionParser) {
		super(expression, attributeResolver, expressionParser);
	}

	public SDFProbabilisticExpression(IExpression<?> expression,
			IAttributeResolver attributeResolver,
			IExpressionParser expressionParser, String expressionString) {
		super(expression, attributeResolver, expressionParser, expressionString);
	}

	public SDFProbabilisticExpression(SDFExpression expression)
			throws SDFExpressionParseException {
		super(expression);
	}

	public SDFProbabilisticExpression(String value,
			IExpressionParser expressionParser)
			throws SDFExpressionParseException {
		super(value, expressionParser);
	}

	public SDFProbabilisticExpression(String URI, String value,
			IAttributeResolver attributeResolver,
			IExpressionParser expressionParser)
			throws SDFExpressionParseException {
		super(URI, value, attributeResolver, expressionParser);
	}

	public void bindDistributions(NormalDistributionMixture[] distributions) {
		if (getMEPExpression() instanceof Constant) {
			return;
		}

		setDistributions(distributions);
	}

	public NormalDistributionMixture getDistributions(int distributionIndex) {
		return this.distributions[distributionIndex];
	}

	private void setDistributions(NormalDistributionMixture[] distributions) {
		this.distributions = distributions;
	}
}
