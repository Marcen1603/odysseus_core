package de.uniol.inf.is.odysseus.markov.operator.transform;

import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.StreamGroupingWithAggregationPO;
import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModel;
import de.uniol.inf.is.odysseus.markov.operator.aggregate.ForwardAggregationFunction;
import de.uniol.inf.is.odysseus.markov.operator.aggregate.ViterbiAggregationFunction;
import de.uniol.inf.is.odysseus.markov.operator.logical.MarkovAO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.relational.base.Tuple;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"rawtypes","unchecked"})
public class TMarkovAORule extends AbstractTransformationRule<MarkovAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(MarkovAO operator, TransformationConfiguration config) {
		StreamGroupingWithAggregationPO<ITimeInterval,IMetaAttributeContainer<ITimeInterval>,IMetaAttributeContainer<ITimeInterval>> aggregatePO = new StreamGroupingWithAggregationPO<ITimeInterval,IMetaAttributeContainer<ITimeInterval>,IMetaAttributeContainer<ITimeInterval>>(operator.getInputSchema(), operator.getOutputSchema(), operator.getGroupingAttributes(),
				operator.getAggregations());
		aggregatePO.setOutputSchema(operator.getOutputSchema());
		aggregatePO.setMetadataMerge(new CombinedMergeFunction<ITimeInterval>());
		
		IGroupProcessor r = new RelationalGroupProcessor(operator.getInputSchema(), operator.getOutputSchema(), operator.getGroupingAttributes(),
				operator.getAggregations());
		
		aggregatePO.setGroupProcessor(r);
		SDFSchema inputSchema = aggregatePO.getInputSchema();
		
		Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations = aggregatePO
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
					IAggregateFunction aggFunction = createAggFunction(p.getE2(), posArray, operator.getHiddenMarkovModel());
					aggregatePO.setInitFunction(p, aggFunction);
					aggregatePO.setMergeFunction(p, aggFunction);
					aggregatePO.setEvalFunction(p, aggFunction);
				}
			}
		}
		replace(operator, aggregatePO, config);
		retract(operator);

	}

	@Override
	public boolean isExecutable(MarkovAO operator, TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "MarkovAO -> AggregatePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	protected IAggregateFunction<Tuple<?>, Tuple<?>> createAggFunction(AggregateFunction key, int[] pos, HiddenMarkovModel hmm) {
		IAggregateFunction<Tuple<?>,Tuple<?>> aggFunc = null;		
		if ((key.getName().equalsIgnoreCase("FORWARD"))) {
			aggFunc = new ForwardAggregationFunction(hmm);
		} else if (key.getName().equalsIgnoreCase("VITERBI")) {
			aggFunc = new ViterbiAggregationFunction(hmm);					
		} else {
			throw new IllegalArgumentException("No such Aggregationfunction");
		}
		return aggFunc;
	}

}
