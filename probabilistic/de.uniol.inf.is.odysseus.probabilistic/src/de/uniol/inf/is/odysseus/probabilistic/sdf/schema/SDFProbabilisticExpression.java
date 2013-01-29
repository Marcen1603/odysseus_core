package de.uniol.inf.is.odysseus.probabilistic.sdf.schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IExpressionParser;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpressionParseException;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.function.AbstractProbabilisticFunction;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class SDFProbabilisticExpression extends SDFExpression {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2862460974715363031L;
	private List<NormalDistributionMixture> distributions;

	public SDFProbabilisticExpression(IExpression<?> expression,
			IAttributeResolver attributeResolver,
			IExpressionParser expressionParser) {
		super(expression, attributeResolver, expressionParser);
		init(expression, null, attributeResolver, expressionParser);
		;
	}

	public SDFProbabilisticExpression(IExpression<?> expression,
			IAttributeResolver attributeResolver,
			IExpressionParser expressionParser, String expressionString) {
		super(expression, attributeResolver, expressionParser, expressionString);
		init(expression, null, attributeResolver, expressionParser);
	}

	public SDFProbabilisticExpression(SDFExpression expression)
			throws SDFExpressionParseException {
		super(expression);
		init(expression.getMEPExpression(), expression.getExpressionString(),
				expression.getAttributeResolver(),
				expression.getExpressionParser());
	}

	public SDFProbabilisticExpression(String value,
			IExpressionParser expressionParser)
			throws SDFExpressionParseException {
		super(value, expressionParser);
		init(null, value, null, expressionParser);
	}

	public SDFProbabilisticExpression(String URI, String value,
			IAttributeResolver attributeResolver,
			IExpressionParser expressionParser)
			throws SDFExpressionParseException {
		super(URI, value, attributeResolver, expressionParser);
		init(null, value, attributeResolver, expressionParser);
	}

	private void init(IExpression<?> expre, String value,
			IAttributeResolver attributeResolver,
			IExpressionParser expressionParser) {
		if (this.getMEPExpression() instanceof IFunction) {
			setDistributions(((AbstractProbabilisticFunction<?>) this
					.getMEPExpression()).getDistributions());
		}
	}

	public void bindDistributions(NormalDistributionMixture[] distributions) {
		if ((getMEPExpression() instanceof Constant) || (getMEPExpression() instanceof Variable)) {
			return;
		}
if (this.distributions==null) {
	System.out.println("DISS NULL");
}
		this.distributions.clear();
		this.distributions.addAll(Arrays.asList(distributions));
	}

	public NormalDistributionMixture getDistributions(int distributionIndex) {
		return this.distributions.get(distributionIndex);
	}

	private void setDistributions(List<NormalDistributionMixture> distributions) {
		this.distributions = distributions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.
	 * SDFExpression#clone()
	 */
	@Override
	public SDFProbabilisticExpression clone() {
		return new SDFProbabilisticExpression(this);
	}
}
