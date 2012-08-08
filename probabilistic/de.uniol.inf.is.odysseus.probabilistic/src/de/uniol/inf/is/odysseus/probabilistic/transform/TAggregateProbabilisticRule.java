package de.uniol.inf.is.odysseus.probabilistic.transform;

import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.AggregateTIPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.probabilistic.Activator;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregate.functions.ProbabilisticConstants;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class TAggregateProbabilisticRule extends
		AbstractTransformationRule<AggregateTIPO<?, ?, ?>> {

	@Override
	public int getPriority() {
		return 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(AggregateTIPO<?, ?, ?> operator,
			TransformationConfiguration config) {
		@SuppressWarnings({ "rawtypes" })
		RelationalGroupProcessor r = new RelationalGroupProcessor(
				operator.getInputSchema(), operator.getInternalOutputSchema(),
				operator.getGroupingAttribute(), operator.getAggregations());
		operator.setGroupProcessor(r);
		SDFSchema inputSchema = operator.getInputSchema();

		Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations = operator
				.getAggregations();

		for (SDFSchema attrList : aggregations.keySet()) {
			if (SDFSchema.subset(attrList, inputSchema)) {
				Map<AggregateFunction, SDFAttribute> funcs = aggregations
						.get(attrList);
				for (Entry<AggregateFunction, SDFAttribute> e : funcs
						.entrySet()) {
					FESortedClonablePair<SDFSchema, AggregateFunction> p = new FESortedClonablePair<SDFSchema, AggregateFunction>(
							attrList, e.getKey());
					int[] posArray = new int[p.getE1().size()];
					for (int i = 0; i < p.getE1().size(); ++i) {
						SDFAttribute attr = p.getE1().get(i);
						posArray[i] = inputSchema.indexOf(attr);
					}
					IAggregateFunctionBuilderRegistry registry = Activator
							.getAggregateFunctionBuilderRegistry();
					IAggregateFunctionBuilder builder = registry.getBuilder(
							config.getDataType(),
							ProbabilisticConstants.NAMESPACE
									+ p.getE2().getName());
					if (builder == null) {
						throw new RuntimeException(
								"Could not find a builder for "
										+ p.getE2().getName());
					}
					@SuppressWarnings("rawtypes")
					IAggregateFunction aggFunction = builder.createAggFunction(
							p.getE2(), posArray);
					operator.setInitFunction(p, aggFunction);
					operator.setMergeFunction(p, aggFunction);
					operator.setEvalFunction(p, aggFunction);
				}
			}
		}
		update(operator);
	}

	@Override
	public boolean isExecutable(AggregateTIPO<?, ?, ?> operator,
			TransformationConfiguration config) {
		if (config.getDataType().equals("relational")) {
			if (operator.getGroupProcessor() == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AggregateTIPO use probabilistic aggregations (IProbabilistic)";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

	@Override
	public Class<? super AggregateTIPO<?, ?, ?>> getConditionClass() {
		return AggregatePO.class;
	}

}
