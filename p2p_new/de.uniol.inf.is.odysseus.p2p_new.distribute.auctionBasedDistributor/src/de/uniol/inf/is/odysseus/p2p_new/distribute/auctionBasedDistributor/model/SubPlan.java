package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.DummyAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;

public class SubPlan {
	private List<ILogicalOperator> operators;
	private List<ILogicalOperator> jxtaSinks;
	private List<ILogicalOperator> jxtaSources;
	private List<ILogicalOperator> dummySinks;
	private List<ILogicalOperator> dummySources;
	private List<ILogicalOperator> sinks;
	private List<ILogicalOperator> sources;
	
	private String destinationName;
	
	public SubPlan(String destinationName, ILogicalOperator ...operators) {
		this.destinationName = destinationName;
		this.operators = Lists.newArrayList();
		this.jxtaSinks = Lists.newArrayList();
		this.jxtaSources = Lists.newArrayList();
		this.dummySinks = Lists.newArrayList();
		this.dummySources = Lists.newArrayList();
		this.sinks = Lists.newArrayList();
		this.sources = Lists.newArrayList();
		addOperators(operators);
	}
	
	public void addOperators(ILogicalOperator ...operators) {
		List<ILogicalOperator> all = getAllOperators();
		all.addAll(Lists.newArrayList(operators));
		this.operators.clear();
		jxtaSinks.clear();
		jxtaSources.clear();
		dummySinks.clear();
		dummySources.clear();
		sinks.clear();
		sources.clear();
		_addOperators(all);
	}

	private void _addOperators(List<ILogicalOperator> operators) {
		for(ILogicalOperator operator : operators) {
			if(operator instanceof JxtaSenderAO && !jxtaSinks.contains(operator)) {
				jxtaSinks.add(operator);
			}
			else if(operator instanceof JxtaReceiverAO && !jxtaSources.contains(operator)) {
				jxtaSources.add(operator);
			}
			else if(operator instanceof DummyAO) {
				if(operator.getSubscriptions().isEmpty() && !dummySinks.contains(operator)) {
					dummySinks.add(operator);
				}
				if(operator.getSubscribedToSource().isEmpty() && !dummySources.contains(operator)) {
					dummySources.add(operator);
				}
			}		
			else {				
				this.operators.add(operator);
				if((operator.getSubscriptions().isEmpty() || 
						oneTargetNotInList(Lists.newArrayList(operators), operator.getSubscriptions()))
						 && !sinks.contains(operator)) {
					sinks.add(operator);
				}
				if((operator.getSubscribedToSource().isEmpty() || 
						oneTargetNotInList(Lists.newArrayList(operators), operator.getSubscribedToSource()))
						 && !sources.contains(operator)){
					sources.add(operator);
				}
			}
		}
	}

	public void removeOperators(ILogicalOperator ...operators) {
		List<ILogicalOperator> all = getAllOperators();
		all.removeAll(Lists.newArrayList(operators));
		this.operators.clear();
		jxtaSinks.clear();
		jxtaSources.clear();
		dummySinks.clear();
		dummySources.clear();
		sinks.clear();
		sources.clear();
		_addOperators(all);		
	}
	

	/**
	 * Determines if the target {@link ILogicalOperator} of any {@link LogicalSubscription} is not in a given 
	 * collection of {@link IlogicalOperator}s.
	 * @see LogicalSubscription#getTarget()
	 * @param operators The given collection of {@link IlogicalOperator}s.
	 * @param subscriptions A collection of {@link LogicalSubscription}s to be checked.
	 * @return true, if any target {@link ILogicalOperator} of <code>subscriptions</code> is not in 
	 * <code>operators</code>.
	 */
	private boolean oneTargetNotInList(List<ILogicalOperator> operators, Collection<LogicalSubscription> subscriptions) {
		
		for(final LogicalSubscription subscription : subscriptions) {
			
			if(!operators.contains(subscription.getTarget()))
				return true;
			
		}		
		return false;		
	}

	public List<ILogicalOperator> getOperators() {
		return Lists.newArrayList(operators);
	}

	public List<ILogicalOperator> getJxtaSinks() {
		return Lists.newArrayList(jxtaSinks);
	}

	public List<ILogicalOperator> getJxtaSources() {
		return Lists.newArrayList(jxtaSources);
	}

	public List<ILogicalOperator> getDummySinks() {
		return Lists.newArrayList(dummySinks);
	}

	public List<ILogicalOperator> getDummySources() {
		return Lists.newArrayList(dummySources);
	}

	public List<ILogicalOperator> getSinks() {
		return Lists.newArrayList(sinks);
	}

	public List<ILogicalOperator> getSources() {
		return Lists.newArrayList(sources);
	}

	public String getDestinationName() {
		return destinationName;
	}	
	
	public String setDestinationName(String name) {
		return destinationName = name;
	}		
	
	public List<ILogicalOperator> getAllOperators() {
		Set<ILogicalOperator> all = Sets.newHashSet();
		all.addAll(jxtaSources);
		all.addAll(jxtaSinks);
		all.addAll(dummySources);
		all.addAll(dummySinks);
		all.addAll(sources);
		all.addAll(sinks);
		all.addAll(operators);
		return Lists.newArrayList(all.toArray(new ILogicalOperator[0]));
	}
	
	@Override
	public String toString() {
		
		String strPart = new String();
		Iterator<ILogicalOperator> iter = this.operators.iterator();
		
		strPart = "{ operators: [";
				
		while(iter.hasNext())
			strPart += iter.next().getName() + ", ";
		
		strPart = strPart.substring(0, strPart.length()-2)+"]";
		
		
		if(this.destinationName != null)
			strPart += ", destination= '" + this.destinationName+"'";
		
		strPart += " }";
		
		return strPart;
		
	}	
	
	public ILogicalOperator getLogicalPlan() {	
		if(!jxtaSinks.isEmpty())
			return jxtaSinks.get(0);
		else if(!dummySinks.isEmpty()) {
			return dummySinks.get(0);
		}
		else {
			return sinks.get(0);
		}		
	}	
	
	public boolean contains(ILogicalOperator operator) {
		return getAllOperators().contains(operator);
	}
	
	public List<ILogicalOperator> findOperatorsByType(Class<?> ...types) {
		List<ILogicalOperator> operators = Lists.newArrayList();
		for(ILogicalOperator op : getAllOperators()) {
			for(Class<?> type : types) {
				if(type.isInstance(op)) {
					operators.add(op);
				}
			}
		}
		return operators;
	}	
}
