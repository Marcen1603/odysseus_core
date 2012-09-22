package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.server.collection.PairMap;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.StreamGroupingWithAggregationPO;
import de.uniol.inf.is.odysseus.securitypunctuation.helper.StandardSecurityEvaluator;

public class SAAggregateTIPO<Q extends ITimeInterval, R extends IMetaAttributeContainer<Q>, W extends IMetaAttributeContainer<Q>> 
extends StreamGroupingWithAggregationPO<Q, R, W> {
	
    private static Logger LOG = LoggerFactory.getLogger(SAAggregateTIPO.class);

	@SuppressWarnings("unchecked")
	private StandardSecurityEvaluator<R> evaluator = new StandardSecurityEvaluator<R>((AbstractPipe<R, R>) this);

	public SAAggregateTIPO(StreamGroupingWithAggregationPO<Q, R, W> agg) {
		super(agg);
	}

	public SAAggregateTIPO(SDFSchema inputSchema, SDFSchema outputSchemaIntern,
			List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations) {
		super(inputSchema, outputSchemaIntern, groupingAttributes, aggregations);
	}

	@Override
	protected synchronized void updateSA(DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,	R elemToAdd) {
		if(evaluator.evaluate(elemToAdd, this.getOwner(), this.getOutputSchema())) {
			super.updateSA(sa, elemToAdd);
			LOG.debug("evaluated");
		} else {
			LOG.debug("nicht evaluated");
		}
	}
	
	@Override
	public void processSecurityPunctuation(ISecurityPunctuation sp, int port) {
		evaluator.addToCache(sp);
		// ???
		this.transferSecurityPunctuation(sp); 
	}
	
	@Override
	public String getName() {
		return "SAAggregate";
	}
}
