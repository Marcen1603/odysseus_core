package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.collection.PairMap;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class StartStopPredicateCoalescePO<M extends ITimeInterval> extends
		AggregatePO<M, IStreamObject<? extends M>, IStreamObject<M>> {

	final static int START = 0;
	final static int END = 1;
	
	@SuppressWarnings("rawtypes")
	private List<IPredicate> predicates;
	private Map<Long, PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<? extends M>>, M>> currentPAs = new HashMap<>();

	public StartStopPredicateCoalescePO(SDFSchema inputSchema,
			SDFSchema outputSchema, List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations,
			@SuppressWarnings("rawtypes") IPredicate startPredicate,
			@SuppressWarnings("rawtypes") IPredicate endPredicate) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations,
				false);
		predicates.add(startPredicate);
		predicates.add(endPredicate);
	}


	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	
	
	@Override
	protected void process_open() throws OpenFailedException {
		getGroupProcessor().init();
		for (IPredicate<?> p : predicates) {
			p.init();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void process_next(IStreamObject<? extends M> object, int port) {
		Long groupID = getGroupProcessor().getGroupID(object);
		PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<? extends M>>, M> pas = currentPAs
				.get(groupID);
		// when calcuation is running
		if (pas != null) {
			if (predicates.get(END).evaluate(object)){
				PairMap<SDFSchema, AggregateFunction, IStreamObject<M>, ?> result = calcEval(pas, false);
				IStreamObject<M> out = getGroupProcessor().createOutputElement(
						groupID, result);
				out.setMetadata(pas.getMetadata());
				transfer(out);
				
				// Clear partial Aggregates
				currentPAs.remove(groupID);
			}else{
				// merge
				PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<? extends M>>, M> newP = calcMerge(
						pas, object, true);
				M newMeta = (M) object.getMetadata().clone();
				// keep start
				newMeta.setStart(pas.getMetadata().getStart());
				newP.setMetadata(newMeta);
				// update pa
				currentPAs.put(groupID, newP);

				// TODO: createHeartbeat(newMeta.getStart());

			}			
		}else{
			if (predicates.get(START).evaluate(object)){
				pas = calcInit(object);
				pas.setMetadata((M) object.getMetadata()
						.clone());
				currentPAs.put(groupID, pas);
			}
		}

	}

}
