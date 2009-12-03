package de.uniol.inf.is.odysseus.rewrite.drools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;

public class RestructHelper {
	public static Collection<ILogicalOperator> removeOperator(
			UnaryLogicalOp remove, boolean reserveOutputSchema) {
		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
		Collection<LogicalSubscription> fathers = remove.getSubscriptions();

		LogicalSubscription child = remove.getSubscribedTo(0);
		// remove Connection between child and op
		remove.unsubscribeTo(child);
		// Subscribe Child to every father of op
		for (LogicalSubscription father : fathers) {
			remove.unsubscribe(father);
			child.getTarget().subscribe(
					father.getTarget(),
					father.getSinkPort(),
					child.getSourcePort(),
					reserveOutputSchema ? remove.getOutputSchema() : child
							.getTarget().getOutputSchema());
			ret.add(father.getTarget());
		}
		ret.add(child.getTarget());
		for (LogicalSubscription a : child.getTarget().getSubscriptions()) {
			System.out.println("NEW SUBPLAN AFTER REMOVE: " + a.getTarget());
		}
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
			ILogicalOperator toInsert, ILogicalOperator after, int sinkPort,
			int toInsertSinkPort, int toInsertSourcePort) {
		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
		LogicalSubscription source = after.getSubscribedTo(sinkPort);
		ret.add(source.getTarget());
		after.unsubscribeTo(source);
		source.getTarget().subscribe(toInsert, toInsertSinkPort,
				source.getSourcePort(), source.getTarget().getOutputSchema());
		toInsert.subscribe(after, sinkPort, toInsertSourcePort, toInsert
				.getOutputSchema());
		ret.add(after);
		return ret;
	}

	public static Collection<ILogicalOperator> simpleOperatorSwitch(
			UnaryLogicalOp father, UnaryLogicalOp son) {
		son.unsubscribe(son.getSubscription());

		LogicalSubscription toDown = son.getSubscribedTo(0);
		son.unsubscribeTo(toDown);

		LogicalSubscription toUp = father.getSubscription();
		father.unsubscribe(toUp);

		father.subscribeTo(toDown.getTarget(), 0, toDown.getSourcePort(),
				toDown.getInputSchema());
		father.subscribe(son, 0, 0, father.getOutputSchema());

		son.subscribe(toUp.getTarget(), toUp.getSinkPort(), 0, son
				.getOutputSchema());

		Collection<ILogicalOperator> toUpdate = new ArrayList<ILogicalOperator>(2);
		toUpdate.add(toDown.getTarget());
		toUpdate.add(toUp.getTarget());
		return toUpdate;
	}

}
