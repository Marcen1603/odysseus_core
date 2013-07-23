package de.uniol.inf.is.odysseus.p2p_new.lb.fragmentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.distribution.IDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

/**
 * The class for abstract data fragmentation strategies. <br />
 * A data fragmentation strategy is able to insert operators for data distribution and data combining.
 * @author Michael Brand
 */
public abstract class AbstractDataFragmentation implements IDataFragmentation {

	@Override
	public Collection<ILogicalOperator> insertOperatorForDistribution(
			Collection<ILogicalOperator> operators, int degreeOfParallelism,
			QueryBuildConfiguration parameters) {
		
		// Preconditions
		Preconditions.checkNotNull(operators, "operators must be not null!");
		Preconditions.checkArgument(operators.size() > 0, "operators must contain at least one operator!");
		Preconditions.checkArgument(degreeOfParallelism > 1, "degreeOfParallelism must be at least 2!");
		Preconditions.checkNotNull(parameters, "parameters must be not null!");
		
		// Inner collection: All subscriptions with the same output port
		// Pair: operator for source access (StreamAO or WindowAO) and those subscriptions, where an operator for fragmentation 
		// shall be inserted (one operator for the whole collection of subscriptions)
		// Outer collection: All pairs of operators and subscriptions, where an operator for fragmentation shall be inserted
		Collection<IPair<ILogicalOperator, Collection<LogicalSubscription>>> sourceAccesses = this.findSourceAccesses(operators);
		
		for(IPair<ILogicalOperator, Collection<LogicalSubscription>> pair : sourceAccesses) {
			
			ILogicalOperator opForDistribution = this.createOperatorForDistribution(degreeOfParallelism, parameters);
			operators.add(opForDistribution);
			this.subscribeOperatorForDistribution(pair.getE1(), opForDistribution, pair.getE2());
			
		}
		
		return operators;
		
	}
	
	@Override
	public abstract Class<? extends ILogicalOperator> getOperatorForDistributionClass();
	
	@Override
	public abstract ILogicalOperator createOperatorForJunction();
	
	@Override
	public abstract Class<? extends ILogicalOperator> getOperatorForJunctionClass();

	@Override
	public abstract String getName();
	
	/**
	 * Finds all {@link StreamAO}s and {@link WindowAO}s and collects them and their outgoing {@link LogicalSubscription}s.
	 * @param operators A collection of all {@link ILogicalOperator}s of the (partial) {@link ILogicalQuery}.
	 * @return Inner collection: All subscriptions with the same output port. <br />
	 * Pair: operator for source access (StreamAO or WindowAO) and those subscriptions, where an operator for fragmentation
	 * shall be inserted (one operator for the whole collection of subscriptions). <br />
	 * Outer collection: All pairs of operators and subscriptions, where an operator for fragmentation shall be inserted.
	 */
	protected Collection<IPair<ILogicalOperator, Collection<LogicalSubscription>>> findSourceAccesses(Collection<ILogicalOperator> operators) {
		
		// Preconditions
		Preconditions.checkNotNull(operators, "operators must be not null!");
		Preconditions.checkArgument(operators.size() > 0, "operators must contain at least one operator!");
		
		// Inner collection: All subscriptions with the same output port
		// Pair: operator for source access (StreamAO or WindowAO) and those subscriptions, where an operator for fragmentation 
		// shall be inserted (one operator for the whole collection of subscriptions)
		// Outer collection: All pairs of operators and subscriptions, where an operator for fragmentation shall be inserted
		Collection<IPair<ILogicalOperator, Collection<LogicalSubscription>>> sourceAccesses = Lists.newArrayList();
		
		for(ILogicalOperator operator : operators) {
			
			if(!(operator instanceof WindowAO) && !(operator instanceof StreamAO))
				continue;
			
			// Mapping of output port -> collection of outgoing subscriptions
			Map<Integer, Collection<LogicalSubscription>> outputPortMapping = Maps.newHashMap();
			
			for(LogicalSubscription subToSink : operator.getSubscriptions()) {
				
				ILogicalOperator sink = subToSink.getTarget();
				
				if(sink instanceof WindowAO)
					continue;
				
				Integer outputPort = subToSink.getSourceOutPort();
				if(!outputPortMapping.containsKey(outputPort))
					outputPortMapping.put(outputPort, new ArrayList<LogicalSubscription>());
				outputPortMapping.get(outputPort).add(subToSink);
				
			}
			
			for(Integer outputPort : outputPortMapping.keySet())
				sourceAccesses.add(new Pair<ILogicalOperator, Collection<LogicalSubscription>>(operator, outputPortMapping.get(outputPort)));
			
		}
		
		return sourceAccesses;
		
	}
	
	/**
	 * Creates an {@link ILogicalOperator} for data fragmentation.
	 * @param degreeOfParallelism The degree of parallelism = number of output ports of the new {@link ILogicalOperator}.
	 * @param parameters The set of all used settings.
	 * @return The new {@link ILogicalOperator} for data fragmentation.
	 */
	protected abstract ILogicalOperator createOperatorForDistribution(int degreeOfParallelism, QueryBuildConfiguration parameters);
	
	/**
	 * Removes old {@link LogicalSubscription}s to <code>sourceAccessOP</code>, 
	 * subscribes <code>distributionOP</code> to <code>sourceAccessOP</code> and 
	 * all targets of the removed {@link LogicalSubscription}s to <code>distributionOP</code>.
	 * @param sourceAccessOP The {@link StreamAO} or {@link WindowAO} after which an {@link ILogicalOperator} for data fragmentation 
	 * shall be inserted.
	 * @param distributionOP The {@link ILogicalOperator} for data fragmentation which shall be inserted.
	 * @param subsToChange A collection of all {@link LogicalSubscription}s which must be changed in order to insert <code>distributionOP</code>.
	 */
	protected void subscribeOperatorForDistribution(ILogicalOperator sourceAccessOP, 
			ILogicalOperator distributionOP, Collection<LogicalSubscription> subsToChange) {
		
		// Preconditions#
		Preconditions.checkNotNull(sourceAccessOP, "sourceAccessOP must be not null!");
		Preconditions.checkNotNull(distributionOP, "distributionOP must be not null!");
		Preconditions.checkNotNull(subsToChange, "subsToChange must be not null!");
		Preconditions.checkArgument(subsToChange.size() > 0, "subsToChange must contain at least one subscription!");
		
		// Remove old subscriptions from the StreamAO or WindowAO
		for(LogicalSubscription sub : subsToChange)
			sourceAccessOP.unsubscribeSink(sub);
		
		// Subscribe the new operator for data fragmentation to the StreamAO or WindowAO
		LogicalSubscription sampleSub = subsToChange.iterator().next();
		sourceAccessOP.subscribeSink(distributionOP, 0, sampleSub.getSourceOutPort(), sampleSub.getSchema());
		
		// Subscribe all operators, which were subscribed to the StreamAO or WindowAO to the new operator for data fragmentation
		Iterator<LogicalSubscription> subIter = subsToChange.iterator();
		for(int index = 0; index < subsToChange.size(); index++) {
			
			LogicalSubscription sub = subIter.next();
			distributionOP.subscribeSink(sub.getTarget(), sub.getSinkInPort(), index, sub.getSchema());
			
		}
		
	}

}