package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.DummyAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Helper;

public class SubPlan {

	private final List<String> destinationNames = Lists.newArrayList();

	private List<ILogicalOperator> operators = Lists.newArrayList();
	private List<ILogicalOperator> jxtaSinks = Lists.newArrayList();
	private List<ILogicalOperator> jxtaSources = Lists.newArrayList();
	private List<ILogicalOperator> dummySinks = Lists.newArrayList();
	private List<ILogicalOperator> dummySources = Lists.newArrayList();
	private List<ILogicalOperator> sinks = Lists.newArrayList();
	private List<ILogicalOperator> sources = Lists.newArrayList();

	public SubPlan(String destinationName, ILogicalOperator... operators) {
		addDestinationName(destinationName);
		addOperators(operators);
	}

	public SubPlan(ILogicalOperator... operators) {
		addOperators(operators);
	}

	public SubPlan(Collection<String> destinationNames, ILogicalOperator[] array) {
		destinationNames.addAll(destinationNames);
		addOperators(array);
	}

	public void addOperators(ILogicalOperator... operators) {
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
		for (ILogicalOperator operator : operators) {
			if (operator instanceof JxtaSenderAO && !jxtaSinks.contains(operator)) {
				jxtaSinks.add(operator);
			} else if (operator instanceof JxtaReceiverAO && !jxtaSources.contains(operator)) {
				jxtaSources.add(operator);
			} else if (operator instanceof DummyAO) {
				if (operator.getSubscriptions().isEmpty() && !dummySinks.contains(operator)) {
					dummySinks.add(operator);
				}
				if (operator.getSubscribedToSource().isEmpty() && !dummySources.contains(operator)) {
					dummySources.add(operator);
				}
			} else {
				this.operators.add(operator);
				if ((operator.getSubscriptions().isEmpty() || Helper.oneTargetNotInList(Lists.newArrayList(operators), operator.getSubscriptions())) && !sinks.contains(operator)) {
					sinks.add(operator);
				}
				if ((operator.getSubscribedToSource().isEmpty() || Helper.oneTargetNotInList(Lists.newArrayList(operators), operator.getSubscribedToSource())) && !sources.contains(operator)) {
					sources.add(operator);
				}
			}
		}
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

	public ImmutableCollection<String> getDestinationNames() {
		return ImmutableList.copyOf(destinationNames);
	}

	public void addDestinationName(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Destination name to add must not be null or empty!");

		if (!destinationNames.contains(name)) {
			destinationNames.add(name);
		}
	}

	public void removeDestinationName(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Destination name to remove must not be null or empty!");

		destinationNames.remove(name);
	}

	public boolean hasDestinationName(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Destination name must not be null or empty!");

		return destinationNames.contains(name);
	}

	public boolean hasLocalDestination() {
		return hasDestinationName("local");
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
		StringBuilder sb = new StringBuilder();

		Iterator<ILogicalOperator> iter = this.operators.iterator();

		sb.append("{ operators: [");

		while (iter.hasNext()) {
			sb.append(iter.next().getName() + "  ");
		}

		if (!this.destinationNames.isEmpty()) {
			sb.append(", destination= " + this.destinationNames);
		}

		sb.append(" }");

		return sb.toString();

	}

	public ILogicalOperator getLogicalPlan() {
		if (!jxtaSinks.isEmpty())
			return jxtaSinks.get(0);
		else if (!dummySinks.isEmpty()) {
			return dummySinks.get(0);
		} else {
			return sinks.get(0);
		}
	}

	public boolean contains(ILogicalOperator operator) {
		return getAllOperators().contains(operator);
	}

	public List<ILogicalOperator> findOperatorsByType(Class<?>... types) {
		List<ILogicalOperator> operators = Lists.newArrayList();
		for (ILogicalOperator op : getAllOperators()) {
			for (Class<?> type : types) {
				if (type.isInstance(op)) {
					operators.add(op);
				}
			}
		}
		return operators;
	}

	public int getDestinationCount() {
		return destinationNames.size();
	}
}
