package de.uniol.inf.is.odysseus.rcp.evaluation.execution;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPreTransformationHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.CollectOperatorLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.logicaloperator.latency.CalcLatencyAO;
import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationModel;
import de.uniol.inf.is.odysseus.rcp.evaluation.processing.logicaloperator.MeasureThroughputAO;

public class EvaluationTransformationHandler implements IPreTransformationHandler {

	@Override
	public String getName() {
		return "EvaluationPreTransformation";
	}

	@Override
	public void preTransform(IServerExecutor executor, ISession caller, ILogicalQuery query, QueryBuildConfiguration config, List<String> handlerParameters, Context context) {		
		Object modelObject = context.get(EvaluationModel.class.getName());
		EvaluationModel model = (EvaluationModel) modelObject;
		if (model != null) {
			if (model.isWithLatency()) {
				addLatencyOperators(query.getLogicalPlan(), caller, model);
			}
			if (model.isWithThroughput()) {
				addThroughputOperators(query.getLogicalPlan(), caller, model);
			}
		} else {
			throw new NullPointerException("This pre transformation handler has no context and was not executed by the evaluation file");
		}

	}

	private void addLatencyOperators(ILogicalOperator logicalPlan, ISession caller, EvaluationModel model) {
		if (logicalPlan instanceof TopAO) {
			List<ILogicalOperator> newChilds = new ArrayList<>();
			for (LogicalSubscription subscription : logicalPlan.getSubscribedToSource()) {
				CalcLatencyAO latency = new CalcLatencyAO();
				latency.subscribeToSource(subscription.getTarget(), 0, 0, subscription.getTarget().getOutputSchema());
				newChilds.add(latency);
			}
			logicalPlan.unsubscribeFromAllSources();
			int inputPort = 0;
			for (ILogicalOperator newChild : newChilds) {
				logicalPlan.subscribeToSource(newChild, inputPort++, 0, newChild.getOutputSchema());
			}
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addThroughputOperators(ILogicalOperator root, ISession caller, EvaluationModel model) {
		CollectOperatorLogicalGraphVisitor<ILogicalOperator> collVisitor = new CollectOperatorLogicalGraphVisitor<ILogicalOperator>(AbstractAccessAO.class);
		GenericGraphWalker collectWalker = new GenericGraphWalker();
		collectWalker.prefixWalk(root, collVisitor);
		for (ILogicalOperator accessAO : collVisitor.getResult()) {
			List<ILogicalOperator> newParents = new ArrayList<>();
			for (LogicalSubscription subscription : accessAO.getSubscriptions()) {
				MeasureThroughputAO mt = new MeasureThroughputAO();
				mt.subscribeSink(subscription.getTarget(), subscription.getSinkInPort(), subscription.getSourceOutPort(), mt.getOutputSchema());
				newParents.add(mt);
			}
			accessAO.unsubscribeFromAllSinks();
			for (ILogicalOperator newParent : newParents) {
				accessAO.subscribeSink(newParent, 0, 0, accessAO.getOutputSchema());
			}
		}
	}

}
