package de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;

public class JoinMultithreadedTransformationStrategy extends
		AbstractMultithreadedTransformationStrategy<JoinAO> {

	@Override
	public String getName() {
		return "JoinMultithreadedTransformationStrategy";
	}

	@Override
	public int evaluateCompatibility(ILogicalOperator operator) {
		if (operator instanceof JoinAO){
			JoinAO joinOperator = (JoinAO) operator;
			if (joinOperator.getPredicate() != null){
				return 100;				
			}
		}
		return 0;
	}

	@Override
	public boolean transform(ILogicalOperator operator, int degreeOfParallelization) {
		JoinAO joinOperator = (JoinAO) operator;
		
		CopyOnWriteArrayList<LogicalSubscription> upstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		upstreamOperatorSubscriptions.addAll(joinOperator
				.getSubscribedToSource());

		CopyOnWriteArrayList<LogicalSubscription> downstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		downstreamOperatorSubscriptions.addAll(joinOperator.getSubscriptions());

		// remove subscriptions
		for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
			ILogicalOperator target = upstreamOperatorSubscription.getTarget();
			target.unsubscribeSink(upstreamOperatorSubscription);
		}

		for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
			ILogicalOperator target = downstreamOperatorSubscription
					.getTarget();
			target.unsubscribeFromSource(downstreamOperatorSubscription);
		}

		// TODO Prädikate können auch anders aufgebaut sein
		List<SDFAttribute> attributes = joinOperator.getPredicate()
				.getAttributes();
		int numberOfFragments = 0;

		List<Pair<HashFragmentAO, Integer>> fragmentsSinkInPorts = new ArrayList<Pair<HashFragmentAO, Integer>>();

		for (SDFAttribute sdfAttribute : attributes) {

			for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
				int indexOfAttribute = upstreamOperatorSubscription.getSchema()
						.indexOf(sdfAttribute);
				if (indexOfAttribute != -1) {
					// if join predicate attribute references on the source from
					// this subscription, create fragment operator
					// Fragment operator
					HashFragmentAO fragment = new HashFragmentAO();
					ArrayList<SDFAttribute> fragmentAttributes = new ArrayList<SDFAttribute>();
					fragmentAttributes.add(sdfAttribute);
					fragment.setAttributes(fragmentAttributes);
					fragment.setNumberOfFragments(degreeOfParallelization);
					fragment.setName("Hash Fragment_" + numberOfFragments);

					Pair<HashFragmentAO, Integer> pair = new Pair<HashFragmentAO, Integer>();
					pair.setE1(fragment);
					pair.setE2(upstreamOperatorSubscription.getSinkInPort());
					fragmentsSinkInPorts.add(pair);

					joinOperator
							.unsubscribeFromSource(upstreamOperatorSubscription);
					fragment.subscribeToSource(upstreamOperatorSubscription
							.getTarget(), 0, upstreamOperatorSubscription
							.getSourceOutPort(), upstreamOperatorSubscription
							.getTarget().getOutputSchema());

					numberOfFragments++;
					break;
				}
			}

		}

		UnionAO union = new UnionAO();
		union.setName("Union");

		int bufferCounter = 0;
		for (int i = 0; i < degreeOfParallelization; i++) {
			JoinAO newJoinOperator = joinOperator.clone();
			newJoinOperator.setName(joinOperator.getName() + "_" + i);
			newJoinOperator.setUniqueIdentifier(joinOperator
					.getUniqueIdentifier() + "_" + i);

			for (Pair<HashFragmentAO, Integer> pair : fragmentsSinkInPorts) {
				BufferAO buffer = new BufferAO();
				buffer.setName("Buffer_" + bufferCounter);
				buffer.setThreaded(true);
				buffer.setMaxBufferSize(10000000);
				bufferCounter++;

				buffer.subscribeToSource(pair.getE1(), 0, i, pair.getE1()
						.getOutputSchema());

				newJoinOperator.subscribeToSource(buffer, pair.getE2(), 0,
						buffer.getOutputSchema());

				union.subscribeToSource(newJoinOperator, i, 0,
						newJoinOperator.getOutputSchema());
			}
		}

		for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
			joinOperator.unsubscribeSink(downstreamOperatorSubscription);
			downstreamOperatorSubscription.getTarget().subscribeToSource(union,
					downstreamOperatorSubscription.getSinkInPort(),
					downstreamOperatorSubscription.getSourceOutPort(),
					union.getOutputSchema());
		}
		
		return true;
	}
}
