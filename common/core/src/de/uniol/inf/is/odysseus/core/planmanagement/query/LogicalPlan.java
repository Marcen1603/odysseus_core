package de.uniol.inf.is.odysseus.core.planmanagement.query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.util.ClearPhysicalSubscriptionsLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.util.CollectOperatorLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.util.FindSinksLogicalVisitor;
import de.uniol.inf.is.odysseus.core.util.FindSourcesLogicalVisitor;
import de.uniol.inf.is.odysseus.core.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.util.RemoveOwnersGraphVisitor;
import de.uniol.inf.is.odysseus.core.util.SetOwnerGraphVisitor;

public class LogicalPlan implements ILogicalPlan {

	private static final long serialVersionUID = -2654431767993962751L;
	final private ILogicalOperator root;
	private List<ILogicalOperator> sources;
	private List<ILogicalOperator> sinks;

	public LogicalPlan(ILogicalOperator logicalPlan) {
		this.root = logicalPlan;
	}

	@Override
	public ILogicalOperator getRoot() {
		return root;
	}

	@Override
	public SDFSchema getOutputSchema() {
		if (root != null) {
			return root.getOutputSchema();
		}
		return null;
	}

	@Override
	public void removeOwner() {
		RemoveOwnersGraphVisitor<ILogicalOperator> visitor = new RemoveOwnersGraphVisitor<ILogicalOperator>();
		GenericGraphWalker<ILogicalOperator> walker = new GenericGraphWalker<ILogicalOperator>();
		walker.prefixWalk(root, visitor);
	}

	@Override
	public void removePhysicalSubscriptions() {
		ClearPhysicalSubscriptionsLogicalGraphVisitor<ILogicalOperator> visitor = new ClearPhysicalSubscriptionsLogicalGraphVisitor<ILogicalOperator>();
		GenericGraphWalker<ILogicalOperator> walker = new GenericGraphWalker<>();
		walker.prefixWalk(root, visitor);
	}

	@Override
	public List<ILogicalOperator> getSources() {
		if (sources == null) {
			FindSourcesLogicalVisitor<ILogicalOperator> collVisitor = new FindSourcesLogicalVisitor<ILogicalOperator>();
			GenericGraphWalker<ILogicalOperator> collectWalker = new GenericGraphWalker<ILogicalOperator>();
			collectWalker.prefixWalk(root, collVisitor);
			sources = collVisitor.getResult();
		}
		return sources;
	}

	@Override
	public List<ILogicalOperator> getSinks() {
		if (sinks == null) {
			sinks = findSinks(root);
		}
		return sinks;
	}

	@Override
	public List<ILogicalOperator> getOperators() {
		return getAllOperators(root);
	}

	@Override
	public Set<ILogicalOperator> findOpsFromType(Class<? extends ILogicalOperator> toFind) {
		CollectOperatorLogicalGraphVisitor<ILogicalOperator> visitor = new CollectOperatorLogicalGraphVisitor<ILogicalOperator>(
				toFind);
		GenericGraphWalker<ILogicalOperator> walker = new GenericGraphWalker<ILogicalOperator>();
		walker.prefixWalk(root, visitor);
		return visitor.getResult();
	}

	@Override
	public ILogicalPlan copyPlan() {
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(
				root.getOwner());
		GenericGraphWalker<ILogicalOperator> walker = new GenericGraphWalker<>();
		walker.prefixWalk(root, copyVisitor);
		return new LogicalPlan(copyVisitor.getResult());
	}

	@Override
	public void setOwner(IOperatorOwner owner) {
		SetOwnerGraphVisitor<ILogicalOperator> visitor = new SetOwnerGraphVisitor<ILogicalOperator>(owner);
		GenericGraphWalker<ILogicalOperator> walker = new GenericGraphWalker<>();
		walker.prefixWalk(root, visitor);
	}

	// ----------------------------------------------------------------------------------------------------
	// Static helper methods
	// ----------------------------------------------------------------------------------------------------

	public static List<ILogicalOperator> getAllOperators(ILogicalOperator operator) {
		List<ILogicalOperator> operators = Lists.newArrayList();
		collectOperatorsImpl(operator, operators);
		return operators;
	}

	private static void collectOperatorsImpl(ILogicalOperator currentOperator, Collection<ILogicalOperator> list) {
		if (!list.contains(currentOperator)) {
			list.add(currentOperator);
			for (final LogicalSubscription subscription : currentOperator.getSubscriptions()) {
				collectOperatorsImpl(subscription.getSink(), list);
			}

			for (final LogicalSubscription subscription : currentOperator.getSubscribedToSource()) {
				collectOperatorsImpl(subscription.getSource(), list);
			}
		}
	}

	public static Collection<ILogicalOperator> getSinks(Collection<ILogicalOperator> operators) {
		Collection<ILogicalOperator> sinks = Lists.newArrayList();
		for (ILogicalOperator operator : operators) {
			if (operator.getSubscriptions().isEmpty()) {
				sinks.add(operator);
			}
		}
		return sinks;
	}

	static public List<ILogicalOperator> findSinks(ILogicalOperator root) {
		FindSinksLogicalVisitor<ILogicalOperator> collVisitor = new FindSinksLogicalVisitor<>();
		GenericGraphWalker<ILogicalOperator> collectWalker = new GenericGraphWalker<ILogicalOperator>();
		collectWalker.prefixWalk(root, collVisitor);
		return collVisitor.getResult();
	}

	public static Collection<ILogicalOperator> getSources(Collection<ILogicalOperator> operators) {
		Collection<ILogicalOperator> sources = Lists.newArrayList();
		for (ILogicalOperator operator : operators) {
			if (operator.getSubscribedToSource().isEmpty()) {
				sources.add(operator);
			}
		}
		return sources;
	}

}
