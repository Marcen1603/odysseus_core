package de.uniol.inf.is.odysseus.net.querydistribute.preprocess.source;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPreProcessor;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryDistributionPreProcessorException;

public class ReplaceStreamPreProcessor implements IQueryDistributionPreProcessor {

	private static final long serialVersionUID = -6040772839698621972L;
	
	private static final String PREPROCESSOR_NAME = "ReplaceStream";
	
	@Override
	public String getName() {
		return PREPROCESSOR_NAME;
	}

	@Override
	public void preProcess(IServerExecutor serverExecutor, ISession caller, ILogicalQuery queryToDistribute, QueryBuildConfiguration config) throws QueryDistributionPreProcessorException {
		ILogicalOperator logicalOperator = queryToDistribute.getLogicalPlan().getRoot();

		Collection<ILogicalOperator> operators = getAllOperators(logicalOperator);
		replaceStreamAOs(operators, serverExecutor, caller);
	}

	private static Collection<ILogicalOperator> getAllOperators(ILogicalOperator operator) {
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

	private static Collection<ILogicalOperator> replaceStreamAOs(Collection<ILogicalOperator> operators, IServerExecutor serverExecutor, ISession activeSession) throws QueryDistributionPreProcessorException {
		Preconditions.checkNotNull(operators);

		List<ILogicalOperator> operatorsToRemove = Lists.newArrayList();
		List<ILogicalOperator> operatorsToAdd = Lists.newArrayList();

		for (ILogicalOperator operator : operators) {

			if (operator instanceof StreamAO) {
				IDataDictionaryWritable dataDictionary = serverExecutor.getDataDictionary(activeSession.getTenant());
				Resource streamname = ((StreamAO) operator).getStreamname();
				
				ILogicalOperator streamPlan = dataDictionary.getStreamForTransformation(streamname, activeSession).getRoot();
				if( streamPlan == null ) {
					streamPlan = dataDictionary.getView(streamname, activeSession).getRoot();
				}
				if( streamPlan == null ) {
					throw new QueryDistributionPreProcessorException("Could not find stream/view " + streamname);
				}

				ILogicalOperator streamPlanCopy = copyLogicalPlan(streamPlan);

				// WORKAROUND:
				streamPlanCopy.unsubscribeFromAllSinks();

				operatorsToRemove.add(operator);
				operatorsToAdd.addAll(getAllOperators(streamPlanCopy));

				setDestinationNames(operator, streamPlanCopy);
				replaceWithSubplan(operator, streamPlanCopy);

			}
		}

		operators.removeAll(operatorsToRemove);
		operators.addAll(operatorsToAdd);

		return operators;
	}

	private static ILogicalOperator copyLogicalPlan(ILogicalOperator originPlan) {
		Preconditions.checkNotNull(originPlan, "Logical plan to copy must not be null!");

		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(originPlan.getOwner());

		GenericGraphWalker<ILogicalOperator> walker = new GenericGraphWalker<>();

		walker.prefixWalk(originPlan, copyVisitor);
		return copyVisitor.getResult();
	}

	private static void setDestinationNames(ILogicalOperator fromOperator, ILogicalOperator toOperator) {
		Collection<ILogicalOperator> streamPlanOperators = getAllOperators(toOperator);
		String destinationName = fromOperator.getDestinationName();
		if (destinationName == null && toOperator.getDestinationName() != null)
			destinationName = toOperator.getDestinationName();
		for (ILogicalOperator streamPlanOperator : streamPlanOperators) {
			streamPlanOperator.setDestinationName(destinationName);
		}
	}

	private static void replaceWithSubplan(ILogicalOperator leafOp, ILogicalOperator newOp) {
		if (leafOp.getSubscribedToSource().size() > 0) {
			throw new IllegalArgumentException("Method can only be called for a leaf");
		}

		for (LogicalSubscription subToSink : leafOp.getSubscriptions()) {
			ILogicalOperator target = subToSink.getSink();

			target.unsubscribeFromSource(leafOp, subToSink.getSinkInPort(), subToSink.getSourceOutPort(), subToSink.getSchema());
			target.subscribeToSource(newOp, subToSink.getSinkInPort(), subToSink.getSourceOutPort(), subToSink.getSchema());
		}
	}
}
