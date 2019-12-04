package de.uniol.inf.is.odysseus.core.planmanagement.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.IBinaryLogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.IUnaryLogicalOperator;
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
import de.uniol.inf.is.odysseus.core.util.SimplePlanPrinter;

public class LogicalPlan implements ILogicalPlan {
	
	static final Logger LOG = LoggerFactory.getLogger(LogicalPlan.class);

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
	public Set<ILogicalOperator> findOpsFromType(Set<Class<? extends ILogicalOperator>> toFind, boolean checkAssignabale) {
		CollectOperatorLogicalGraphVisitor<ILogicalOperator> visitor = new CollectOperatorLogicalGraphVisitor<ILogicalOperator>(
				toFind, checkAssignabale);
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
	
	@Override
	public String getPlanAsString(boolean detailed) {
		return getPlanAsString(this, detailed);
	}

	// ----------------------------------------------------------------------------------------------------
	// Static helper methods
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Update all output schema the reads input from startAt logical operator
	 * @param toInsert
	 */
	public static void recalcOutputSchemas(ILogicalOperator startAt) {
		recalcOutputSchemas(startAt,true);
	}
	
	/**
	 * Update all output schema the reads input from startAt logical operator
	 * @param toInsert
	 */
	public static void recalcOutputSchemas(ILogicalOperator startAt, boolean includingStart) {
		if (includingStart) {
			startAt.recalcOutputSchema();
		}
		Collection<LogicalSubscription> targets = startAt.getSubscriptions();
		for (LogicalSubscription sub: targets) {
			recalcOutputSchemas(sub.getSink());
		}	
	}
	
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
	
	public static Collection<ILogicalOperator> removeOperator(IBinaryLogicalOperator remove, boolean reserveOutputSchema) {
        List<ILogicalOperator> ret = new ArrayList<>();
        Collection<LogicalSubscription> fathers = remove.getSubscriptions();
        LogicalSubscription left = remove.getSubscribedToSource(0);
        LogicalSubscription right = remove.getSubscribedToSource(1);

        // remove Connection between child and op
        remove.unsubscribeFromSource(left);
        remove.unsubscribeFromSource(right);

        // Subscribe Child to every father of op
        for (LogicalSubscription father : fathers) {
            remove.unsubscribeSink(father);
            left.getSource().subscribeSink(father.getSink(), father.getSinkInPort(), left.getSourceOutPort(), reserveOutputSchema ? remove.getOutputSchema() : left.getSource().getOutputSchema());
            ret.add(father.getSink());
        }
        for (LogicalSubscription father : fathers) {
            remove.unsubscribeSink(father);
            right.getSource().subscribeSink(father.getSink(), father.getSinkInPort(), right.getSourceOutPort(), reserveOutputSchema ? remove.getOutputSchema() : right.getSource().getOutputSchema());
            ret.add(father.getSink());
        }
        // prevents duplicate entry if child.getTarget=father.getTarget
        if (!ret.contains(left.getSource())) {
            ret.add(left.getSource());
        }
        if (!ret.contains(right.getSource())) {
            ret.add(right.getSource());
        }
        return ret;
    }

	public static Collection<ILogicalOperator> removeOperator(
			IUnaryLogicalOperator remove, boolean reserveOutputSchema) {
		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
		Collection<LogicalSubscription> fathers = remove.getSubscriptions();
		LogicalSubscription child = remove.getSubscribedToSource(0);
		// remove Connection between child and op
		remove.unsubscribeFromSource(child);
		// Subscribe Child to every father of op
		for (LogicalSubscription father : fathers) {
			remove.unsubscribeSink(father);
			child.getSource().subscribeSink(
					father.getSink(),
					father.getSinkInPort(),
					child.getSourceOutPort(),
					reserveOutputSchema ? remove.getOutputSchema() : child
							.getSource().getOutputSchema());
			ret.add(father.getSink());
		}
		// prevents duplicate entry if child.getTarget=father.getTarget
		if (!ret.contains(child.getSource())) {
			ret.add(child.getSource());
		}
		// for (LogicalSubscription a : child.getTarget().getSubscriptions()) {
		// LoggerFactory.getLogger(LogicalPlan.class).debug(
		// "New subplan after remove: " + a.getTarget());
		// }
		return ret;
	}

	/**
	 * Insert an operator in the tree at some special point and update all
	 * subscriptions i.e. the new Operator gets all subscriptions currently
	 * bound to the after operator (looking from root!) and create a new
	 * subscription from toInsert to after
	 *
	 * @param toInsert
	 *            Operator that should be inserted as child of the after
	 *            operator
	 * @param after
	 * @return
	 */
	public static Collection<ILogicalOperator> insertOperator(
			ILogicalOperator toInsert, ILogicalOperator after, int sinkInPort,
			int toInsertsinkInPort, int toInsertsourceOutPort) {
		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
		LogicalSubscription source = after.getSubscribedToSource(sinkInPort);
		ret.add(source.getSource());
		after.unsubscribeFromSource(source);
		source.getSource()
				.subscribeSink(toInsert, toInsertsinkInPort,
						source.getSourceOutPort(),
						source.getSource().getOutputSchema());
		toInsert.subscribeSink(after, sinkInPort, toInsertsourceOutPort,
				toInsert.getOutputSchema());
		ret.add(after);
		return ret;
	}

	/**
	 * Inserts a new logical operator (toInsert) before the operator before
	 * (e.g. closer to the root!)
	 *
	 * @param toInsert
	 * @param before
	 * @return
	 */
	public static Collection<ILogicalOperator> insertOperatorBefore(
			ILogicalOperator toInsert, ILogicalOperator before) {
		List<ILogicalOperator> ret = new ArrayList<>();
		Collection<LogicalSubscription> subs = before.getSubscriptions();
		for (LogicalSubscription sub : subs) {
			before.unsubscribeSink(sub);
			// What about the source out port
			toInsert.subscribeSink(sub.getSink(), sub.getSinkInPort(),
					sub.getSourceOutPort(), sub.getSchema());
			ret.add(sub.getSink());
		}
		toInsert.subscribeToSource(before, 0, 0, before.getOutputSchema());
		ret.add(before);
		ret.add(toInsert);
		return ret;

	}

	/**
	 * Inserts a new logical operator (toInsert) before the operator before
	 * (e.g. closer to the root!)
	 *
	 * @param toInsert
	 * @param before
	 * @return
	 */
	public static Collection<ILogicalOperator> insertOperatorBefore2(
			ILogicalOperator toInsert, ILogicalOperator before) {
		List<ILogicalOperator> ret = new ArrayList<>();
		Collection<LogicalSubscription> subs = before.getSubscriptions();
		for (LogicalSubscription sub : subs) {
			before.unsubscribeSink(sub);
			// What about the source out port
			toInsert.subscribeSink(sub.getSink(), sub.getSinkInPort(),
					sub.getSourceOutPort(), toInsert.getInputSchema(0));
			ret.add(sub.getSink());
		}
		toInsert.subscribeToSource(before, 0, 0, before.getOutputSchema());
		ret.add(before);
		ret.add(toInsert);
		return ret;

	}

	/**
	 * Inserts a new logical operator (toInsert) before the operator before
	 * (e.g. closer to the root!)
	 *
	 * @param toInsert
	 * @param before
	 * @return
	 */
	public static Collection<ILogicalOperator> insertOperatorBefore(
			ILogicalOperator toInsert, ILogicalOperator before,  int sinkInPort, int sourceOutPort, int insertOpInputPort) {
		List<ILogicalOperator> ret = new ArrayList<>();
		Collection<LogicalSubscription> subs = before.getSubscriptions();

		for (LogicalSubscription sub : subs) {
			if(sourceOutPort == sub.getSourceOutPort()){
				before.unsubscribeSink(sub);
				// What about the source out port
				toInsert.subscribeSink(sub.getSink(), sub.getSinkInPort(),
						sub.getSourceOutPort(), sub.getSchema());
				ret.add(sub.getSink());
			}
		}
		toInsert.subscribeToSource(before, insertOpInputPort, sourceOutPort, before.getOutputSchema());
		ret.add(before);
		ret.add(toInsert);
		return ret;

	}

	public static Collection<ILogicalOperator> simpleOperatorSwitch(
			IUnaryLogicalOperator father, IUnaryLogicalOperator son) {
		// TODO: Can there be more than one father??
		if (son.getSubscriptions().size() != 1) {
			LOG.error("MAY NOT HAPPEN IN SIMPLE SWITCH!!!");
			LOG.error("FATHER: " + father);
			LOG.error("SON HAS MORE THAN ONE FATHER!!!!");
			LOG.error("SON: " + son);
			for (LogicalSubscription sub : son.getSubscriptions()) {
				System.out.println(sub);
			}
		}
		son.unsubscribeSink(son.getSubscriptions().iterator().next());

		LogicalSubscription toDown = son.getSubscribedToSource(0);
		son.unsubscribeFromSource(toDown);

		// TODO: Can there be more than one father??
		if (father.getSubscriptions().size() != 1) {
			LOG.error("THIS MAY NOT HAPPEN");
			for (LogicalSubscription sub : son.getSubscriptions()) {
				System.out.println(sub);
			}
		}
		LogicalSubscription toUp = father.getSubscriptions().iterator().next();
		father.unsubscribeSink(toUp);

		father.subscribeToSource(toDown.getSource(), 0,
				toDown.getSourceOutPort(), toDown.getSchema());
		father.subscribeSink(son, 0, 0, father.getOutputSchema());

		son.subscribeSink(toUp.getSink(), toUp.getSinkInPort(), 0,
				son.getOutputSchema());

		Collection<ILogicalOperator> toUpdate = new ArrayList<ILogicalOperator>(
				2);
		toUpdate.add(toDown.getSource());
		toUpdate.add(toUp.getSink());
		return toUpdate;
	}

	public static Collection<ILogicalOperator> replace(ILogicalOperator oldOP,
			ILogicalOperator newOp) {
		Collection<ILogicalOperator> touched = new LinkedList<>();

		Collection<LogicalSubscription> subscriptions = oldOP
				.getSubscriptions();

		if (newOp.getSubscriptions().size() > 0
				|| newOp.getSubscribedToSource().size() > 0) {
			throw new IllegalArgumentException(
					"Replacement operator is not allowed to be connected!");
		}

		// Replace subscriptions to sinks
		for (LogicalSubscription s : subscriptions) {
			oldOP.unsubscribeSink(s);
			newOp.subscribeSink(s.getSink(), s.getSinkInPort(),
					s.getSourceOutPort(), s.getRealSchema());
			touched.add(s.getSink());
		}

		// Replace subscriptions to source
		subscriptions = new ArrayList<>(oldOP.getSubscribedToSource());
		for (LogicalSubscription s : subscriptions) {
			oldOP.unsubscribeFromSource(s);
			newOp.subscribeToSource(s.getSource(), s.getSinkInPort(),
					s.getSourceOutPort(), s.getRealSchema());
			touched.add(s.getSource());
		}

		return touched;
	}

	/**
	 * Replaces a logical leaf operator by a subplan.
	 *
	 * @param leafOp
	 *            The logical operator to be replaced.
	 * @param newOp
	 *            The subplan to be inserted instead.
	 */
	public static void replaceWithSubplan(ILogicalOperator leafOp,
			ILogicalOperator newOp) {

		if (leafOp.getSubscribedToSource().size() > 0) {
			throw new IllegalArgumentException(
					"Method can only be called for a leaf");
		}

		// change all subscriptions, which were from oldOp to its targets
		for (LogicalSubscription subToSink : leafOp.getSubscriptions()) {

			ILogicalOperator target = subToSink.getSink();

			target.unsubscribeFromSource(leafOp, subToSink.getSinkInPort(),
					subToSink.getSourceOutPort(), subToSink.getSchema());
			target.subscribeToSource(newOp, subToSink.getSinkInPort(),
					subToSink.getSourceOutPort(), subToSink.getSchema());

		}

	}


	/**
	 * Searches for a {@link LogicalSubscription} between two
	 * {@link ILogicalOperator}s.
	 *
	 * @param source
	 *            The relative source for the {@link LogicalSubscription}.
	 * @param sink
	 *            The relative sink for the {@link LogicalSubscription}.
	 * @return The {@link LogicalSubscription} between <code>source</code> and
	 *         <code>sink</code> or null, if there is no such subscription.
	 */
	public static LogicalSubscription determineSubscription(
			ILogicalOperator source, ILogicalOperator sink) {

		for (final LogicalSubscription subscription : source.getSubscriptions()) {

			if (subscription.getSink().equals(sink))
				return subscription;

		}

		return null;

	}
	
	static public String getPlanAsString(ILogicalPlan logicalPlan, boolean detailed) {
		SimplePlanPrinter<ILogicalOperator> planPrinter = new SimplePlanPrinter<ILogicalOperator>(detailed);
		return planPrinter.createString(logicalPlan.getRoot());
	}


}
