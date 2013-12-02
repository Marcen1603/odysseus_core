package de.uniol.inf.is.odysseus.peer.distribute.util;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.peer.distribute.service.PQLGeneratorService;
import de.uniol.inf.is.odysseus.peer.distribute.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.peer.distribute.service.SessionManagementService;

public final class LogicalQueryHelper {

	private static final String PQL_PARSER_ID = "PQL";

	private LogicalQueryHelper() {
		// do not instantiate this
	}
	
	public static ILogicalQuery copyLogicalQuery(ILogicalQuery originQuery) {
		Preconditions.checkNotNull(originQuery, "Logical query to copy must not be null!");

		ILogicalQuery copy = new LogicalQuery(PQL_PARSER_ID, copyLogicalPlan(originQuery.getLogicalPlan()), originQuery.getPriority());

		copy.setName(originQuery.getName());
		copy.setQueryText(PQLGeneratorService.get().generatePQLStatement(copy.getLogicalPlan()));
		copy.setUser(originQuery.getUser());

		return copy;
	}
	
	public static ILogicalOperator copyLogicalPlan(ILogicalOperator originPlan) {
		Preconditions.checkNotNull(originPlan, "Logical plan to copy must not be null!");

		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(originPlan.getOwner());

		GenericGraphWalker<ILogicalOperator, ILogicalOperator, LogicalSubscription> walker = new GenericGraphWalker<>();

		walker.prefixWalk(originPlan, copyVisitor);
		return copyVisitor.getResult();
	}
	
	public static Collection<ILogicalOperator> getAllOperators( ILogicalQuery plan ) {
		return getAllOperators(plan.getLogicalPlan());
	}
	
	public static Collection<ILogicalOperator> getAllOperators( ILogicalOperator operator ) {
		List<ILogicalOperator> operators = Lists.newArrayList();
		collectOperatorsImpl(operator, operators);
		return operators;
	}
	
	private static void collectOperatorsImpl(ILogicalOperator currentOperator, Collection<ILogicalOperator> list) {
		if (!list.contains(currentOperator)) {
			list.add(currentOperator);
			for (final LogicalSubscription subscription : currentOperator.getSubscriptions()) {
				collectOperatorsImpl(subscription.getTarget(), list);
			}

			for (final LogicalSubscription subscription : currentOperator.getSubscribedToSource()) {
				collectOperatorsImpl(subscription.getTarget(), list);
			}
		}
	}
	
	public static Collection<ILogicalOperator> replaceStreamAOs(Collection<ILogicalOperator> operators) {
		Preconditions.checkNotNull(operators);

		final List<ILogicalOperator> operatorsToRemove = Lists.newArrayList();
		final List<ILogicalOperator> operatorsToAdd = Lists.newArrayList();

		for (ILogicalOperator operator : operators) {

			if (operator instanceof StreamAO) {
				ILogicalOperator streamPlan = ServerExecutorService.getDataDictionary(SessionManagementService.getActiveSession().getTenant()).getStreamForTransformation(
						((StreamAO) operator).getStreamname(), SessionManagementService.getActiveSession());

				ILogicalOperator streamPlanCopy = copyLogicalPlan(streamPlan);

				replaceWithSubplan(operator, streamPlanCopy);
				operatorsToRemove.add(operator);
				operatorsToAdd.add(streamPlanCopy);
			}
		}

		operators.removeAll(operatorsToRemove);
		operators.addAll(operatorsToAdd);

		return operators;

	}
	
	public static void replaceWithSubplan(ILogicalOperator leafOp, ILogicalOperator newOp) {
		if (leafOp.getSubscribedToSource().size() > 0) {
			throw new IllegalArgumentException("Method can only be called for a leaf");
		}

		for (LogicalSubscription subToSink : leafOp.getSubscriptions()) {
			ILogicalOperator target = subToSink.getTarget();

			target.unsubscribeFromSource(leafOp, subToSink.getSinkInPort(), subToSink.getSourceOutPort(), subToSink.getSchema());
			target.subscribeToSource(newOp, subToSink.getSinkInPort(), subToSink.getSourceOutPort(), subToSink.getSchema());
		}
	}

	public static void removeTopAOs(Collection<ILogicalOperator> operators) {
		final List<ILogicalOperator> operatorsToRemove = Lists.newArrayList();

		for (final ILogicalOperator operator : operators) {
			if (operator instanceof TopAO) {
				operator.unsubscribeFromAllSources();
				operatorsToRemove.add(operator);
			}
		}

		for (final ILogicalOperator operatorToRemove : operatorsToRemove) {
			operators.remove(operatorToRemove);
		}
	}
}
