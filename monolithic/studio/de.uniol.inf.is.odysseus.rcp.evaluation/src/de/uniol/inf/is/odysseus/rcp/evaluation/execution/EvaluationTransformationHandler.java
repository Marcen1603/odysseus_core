package de.uniol.inf.is.odysseus.rcp.evaluation.execution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.FileSinkAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPreTransformationHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.CollectOperatorLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.logicaloperator.latency.CalcLatencyAO;
import de.uniol.inf.is.odysseus.logicaloperator.latency.LatencyToPayloadAO;
import de.uniol.inf.is.odysseus.rcp.evaluation.processing.logicaloperator.MeasureThroughputAO;

public class EvaluationTransformationHandler implements IPreTransformationHandler {

	@Override
	public String getName() {
		return "EvaluationPreTransformation";
	}

	@Override
	public void preTransform(IServerExecutor executor, ISession caller, ILogicalQuery query, QueryBuildConfiguration config, List<String> handlerParameters, Context context) {		
		Object modelObject = context.get(EvaluationRun.class.getName());
		EvaluationRun run = (EvaluationRun) modelObject;
		if (run != null) {
			if (run.getContext().getModel().isWithLatency()) {
				addLatencyOperators(query.getLogicalPlan(), caller, run);
			}
			if (run.getContext().getModel().isWithThroughput()) {
				addThroughputOperators(query.getLogicalPlan(), caller, run);
			}
		} else {
			throw new NullPointerException("This pre transformation handler has no context and was not executed by the evaluation file");
		}

	}

	private void addLatencyOperators(ILogicalOperator logicalPlan, ISession caller, EvaluationRun run) {
		if (logicalPlan instanceof TopAO) {
			List<ILogicalOperator> newChilds = new ArrayList<>();
			for (LogicalSubscription subscription : logicalPlan.getSubscribedToSource()) {
				ILogicalOperator root = subscription.getTarget();
				CalcLatencyAO latency = new CalcLatencyAO();
				latency.subscribeToSource(root, 0, 0, root.getOutputSchema());								
				LatencyToPayloadAO ltp = new LatencyToPayloadAO(false, true);
				ltp.subscribeToSource(latency, 0, 0, latency.getOutputSchema());
				FileSinkAO fileAO = new FileSinkAO();
				fileAO.setSinkType("CSV");
				fileAO.setPrintMetadata(false);
				fileAO.setLineNumbering(true);
				fileAO.setNumbFormatter("##################################");
				fileAO.setFloatFormatter("##################################");
				
				fileAO.setFilename(run.createLatencyResultPath(root));
				fileAO.subscribeToSource(ltp, 0, 0, ltp.getOutputSchema());
				
				newChilds.add(fileAO);
				
				
			}
			logicalPlan.unsubscribeFromAllSources();
			int inputPort = 0;
			for (ILogicalOperator newChild : newChilds) {
				logicalPlan.subscribeToSource(newChild, inputPort++, 0, newChild.getOutputSchema());
			}
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addThroughputOperators(ILogicalOperator root, ISession caller, EvaluationRun run) {
		
		Set<Class<? extends ILogicalOperator>> set = new HashSet<>();
		set.add(AbstractAccessAO.class);
		set.add(StreamAO.class);
		CollectOperatorLogicalGraphVisitor<ILogicalOperator> collVisitor = new CollectOperatorLogicalGraphVisitor<ILogicalOperator>(set);
		GenericGraphWalker collectWalker = new GenericGraphWalker();
		collectWalker.prefixWalk(root, collVisitor);		
		for (ILogicalOperator accessAO : collVisitor.getResult()) {
			
			List<LogicalSubscription> nextSinks = new ArrayList<>(accessAO.getSubscriptions());
			accessAO.unsubscribeFromAllSinks();	
			MeasureThroughputAO mt = new MeasureThroughputAO();
			mt.setEach(run.getContext().getModel().getMeasureThrougputEach());
			mt.subscribeToSource(accessAO, 0, 0, accessAO.getOutputSchema());
			mt.setFilename(run.createThroughputResultPath(mt.getInputAO()));			
			for (LogicalSubscription sub : nextSinks) {				
				mt.subscribeSink(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(), mt.getOutputSchema());										
			}			
			
		}
	}

}
