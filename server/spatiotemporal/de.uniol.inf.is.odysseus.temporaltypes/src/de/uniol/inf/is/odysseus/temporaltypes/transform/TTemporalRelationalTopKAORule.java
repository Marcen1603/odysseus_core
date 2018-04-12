package de.uniol.inf.is.odysseus.temporaltypes.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopKAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.NoGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.AbstractRelationalTopKPO;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.RelationalDynamicScoreTopKPO;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.RelationalTopKPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.temporaltypes.expressions.TemporalRelationalExpression;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.physicalopertor.TemporalRelationalTopKPO;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTemporalRelationalTopKAORule extends AbstractTransformationRule<TopKAO> {

	@Override
	public void execute(TopKAO operator, TransformationConfiguration config) throws RuleException {
		IGroupProcessor<Tuple<ITimeInterval>, Tuple<ITimeInterval>> groupProcessor;
		List<SDFAttribute> grouping = operator.getGroupingAttributes();
		if (grouping != null) {
			groupProcessor = new RelationalGroupProcessor<>(operator.getInputSchema(0), operator.getOutputSchema(),
					grouping, null, operator.isFastGrouping());
		} else {
			groupProcessor = new NoGroupProcessor<Tuple<ITimeInterval>, Tuple<ITimeInterval>>();
		}

		SDFExpression setupFunction = operator.getSetupFunction() != null ? operator.getSetupFunction().expression
				: null;
		SDFExpression tearDownFunction = operator.getTearDownFunction() != null
				? operator.getTearDownFunction().expression
				: null;
		SDFExpression preScoreFunction = operator.getPreScoreFunction() != null
				? operator.getPreScoreFunction().expression
				: null;
		SDFExpression scoringFunction = operator.getScoringFunction().expression;
		SDFExpression cleanUpPredicate = operator.getCleanupPredicate() != null
				? operator.getCleanupPredicate().expression
				: null;

		TemporalRelationalExpression<IValidTimes> temporalSetupExpression = null;
		if (expressionHasTemporalAttribute(setupFunction)) {
			temporalSetupExpression = new TemporalRelationalExpression<>(setupFunction);
		}

		AbstractRelationalTopKPO<Tuple<ITimeInterval>, ITimeInterval> po;

		po = new TemporalRelationalTopKPO<>(operator.getInputSchema(0), operator.getOutputSchema(),
				temporalSetupExpression, preScoreFunction, scoringFunction, tearDownFunction, cleanUpPredicate,
				operator.getK(), operator.isDescending(), operator.isSuppressDuplicates(),
				operator.getUniqueAttributes(), groupProcessor, operator.isTriggerOnlyByPunctuation(),
				operator.isAddScore());

		po.setOrderByTimestamp(operator.isTiWithTimestamp());
		defaultExecute(operator, po, config, true, true);

	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public int getPriority() {
		// The priority needs to be higher than the priority of the normal rule.
		return 1;
	}

	@Override
	public boolean isExecutable(TopKAO operator, TransformationConfiguration config) {
		/*
		 * Only use this rule if there is at least one expression with a temporal
		 * attribute.
		 */
		return operator.isAllPhysicalInputSet()
				&& this.containsExpressionWithTemporalAttribute(this.getOperatorExpressions(operator));
	}

	private List<SDFExpression> getOperatorExpressions(TopKAO operator) {
		List<SDFExpression> expressions = new ArrayList<>(4);
		expressions.add(operator.getPreScoreFunction().expression);
		expressions.add(operator.getScoringFunction().expression);
		expressions.add(operator.getSetupFunction().expression);
		expressions.add(operator.getTearDownFunction().expression);
		return expressions;
	}

	/**
	 * Checks if at least one expression has a temporal attribute.
	 * 
	 * @param expressions
	 *            The expressions to test
	 * @return True, if at least one expression has a temporal attribute, false
	 *         otherwise
	 */
	protected boolean containsExpressionWithTemporalAttribute(List<SDFExpression> expressions) {
		for (SDFExpression expression : expressions) {
			if (expression != null && expressionHasTemporalAttribute(expression)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if an expression contains a temporal attribute
	 * 
	 * @param expression
	 *            The expression to check
	 * @return True, if is has a temporal attribute, false otherwise
	 */
	protected boolean expressionHasTemporalAttribute(SDFExpression expression) {
		if (expression == null) {
			return false;
		}
		for (SDFAttribute attribute : expression.getAllAttributes()) {
			if (TemporalDatatype.isTemporalAttribute(attribute)) {
				return true;
			}
		}
		return false;
	}

}
