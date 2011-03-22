package de.uniol.inf.is.odysseus.markov.operator.logical;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModel;
import de.uniol.inf.is.odysseus.markov.operator.aggregate.ForwardAggregationFunction;
import de.uniol.inf.is.odysseus.markov.operator.aggregate.ViterbiAggregationFunction;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IEvaluator;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalTupleGroupingHelper;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class MarkovGroupingHelper<T extends IMetaAttribute> extends RelationalTupleGroupingHelper<T> {

	private HiddenMarkovModel hmm;
	
	public MarkovGroupingHelper(SDFAttributeList inputSchema, SDFAttributeList outputSchema, List<SDFAttribute> groupingAttributes,
			Map<SDFAttribute, Map<AggregateFunction, SDFAttribute>> aggregations, HiddenMarkovModel hmm) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations);
		this.hmm = hmm;
	}
	
	protected IEvaluator<RelationalTuple<?>> createAggFunction(AggregateFunction key, int[] pos) {
		IEvaluator<RelationalTuple<?>> aggFunc = null;		
		if ((key.getName().equalsIgnoreCase("FORWARD"))) {
			aggFunc = new ForwardAggregationFunction(this.hmm);
		} else if (key.getName().equalsIgnoreCase("VITERBI")) {
			aggFunc = new ViterbiAggregationFunction(this.hmm);					
		} else {
			throw new IllegalArgumentException("No such Aggregationfunction");
		}
		return aggFunc;
	}
	
	public HiddenMarkovModel getHMM(){
		return this.hmm;
	}

}
