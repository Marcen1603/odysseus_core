package de.uniol.inf.is.odysseus.timeseries.transform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recommendation.transform.FindAttributeHelper;
import de.uniol.inf.is.odysseus.recommendation.util.TupleSchemaHelper;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.timeseries.logicaloperator.ForecastVarianceAO;
import de.uniol.inf.is.odysseus.timeseries.physicaloperator.ForecastVariancePO;
import de.uniol.inf.is.odysseus.timeseries.physicaloperator.ForecastVariancePO.ForecastVarianceTupleSchema;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christoph Schröer
 *
 */
@Deprecated
public class TForecastVarianceAORule extends AbstractTransformationRule<ForecastVarianceAO> {

	public TForecastVarianceAORule() {
		super();
	}

	@Override
	public void execute(final ForecastVarianceAO operator, final TransformationConfiguration config)
			throws RuleException {

		if (operator.getSubscribedToSource().size() > 1) {
			insertJoinBefore(operator);
		}

		// get the attribute ports
		final int modelPort = FindAttributeHelper.findPortWithAttribute(operator, operator.getModelAttribute());
		final int elementsPort = FindAttributeHelper.findPortWithAttribute(operator, operator.getResidualAttribute());

		if (modelPort == -1) {
			throw new IllegalArgumentException("Model port not found.");
		}
		if (elementsPort == -1) {
			throw new IllegalArgumentException("Elements port not found.");
		}

		// get the attribute indizes in tuple
		final int modelAttributeIndex = FindAttributeHelper.findAttributeIndex(operator, operator.getModelAttribute(),
				modelPort);
		final int residualAttibuteIndex = FindAttributeHelper.findAttributeIndex(operator,
				operator.getResidualAttribute(), elementsPort);

		final Map<ForecastVarianceTupleSchema, Integer> map = new HashMap<ForecastVarianceTupleSchema, Integer>();

		map.put(ForecastVarianceTupleSchema.MODEL, modelAttributeIndex);
		map.put(ForecastVarianceTupleSchema.RESIDUAL, residualAttibuteIndex);
		final TupleSchemaHelper<ITimeInterval, ForecastVarianceTupleSchema> tsh = new TupleSchemaHelper<>(map);

		final ForecastVariancePO po = new ForecastVariancePO(tsh, operator.getForecastHorizon());

		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(final ForecastVarianceAO operator, final TransformationConfiguration config) {
		// QN: Welche Bedeutung hat dies?
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {

		// QN: Welche Bedeutung hat dies?
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	/**
	 * QN {@see TPredictRatingAORule}
	 */
	private void insertJoinBefore(final ForecastVarianceAO operator) {
		final JoinAO join = new JoinAO();

		// LogicalPlan.insertOperator(join, operator, 0, 0, 0);
		// LogicalPlan.insertOperator(join, operator, 1, 1, 0);

		final Collection<LogicalSubscription> subscriptions = new ArrayList<>(operator.getSubscribedToSource());
		for (final LogicalSubscription s : subscriptions) {
			operator.unsubscribeFromSource(s);
			join.subscribeToSource(s.getSource(), s.getSinkInPort(), s.getSourceOutPort(), s.getSchema());
		}
		operator.subscribeToSource(join, 0, 0, join.getOutputSchema());

		insert(join);

	}
}
