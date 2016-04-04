package de.uniol.inf.is.odysseus.rcp.evaluation.execution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSink;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPreTransformationHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.CollectOperatorLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.latency.Latency;
import de.uniol.inf.is.odysseus.logicaloperator.latency.CalcLatencyAO;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.rcp.evaluation.plot.MeasurementFileUtil;
import de.uniol.inf.is.odysseus.rcp.evaluation.processing.logicaloperator.MeasureThroughputAO;
import de.uniol.inf.is.odysseus.systemload.logicaloperator.SystemLoadToPayloadAO;

public class EvaluationTransformationHandler implements IPreTransformationHandler {

	@Override
	public String getName() {
		return "EvaluationPreTransformation";
	}

	@Override
	public void preTransform(IServerExecutor executor, ISession caller, ILogicalQuery query,
			QueryBuildConfiguration config, List<Pair<String, String>> handlerParameters, Context context) {
		Object modelObject = context.get(EvaluationRun.class.getName());
		EvaluationRun run = (EvaluationRun) modelObject;
		if (run != null) {
			if (run.getContext().getModel().isWithLatency()) {
				addLatencyOperators(query.getLogicalPlan(), caller, run);
			}
			if (run.getContext().getModel().isWithThroughput()) {
				addThroughputOperators(query.getLogicalPlan(), caller, run);
			}
			if (run.getContext().getModel().isWithResource()) {
				addResourceOperators(query.getLogicalPlan(), caller, run);
			}
		} else {
			throw new NullPointerException(
					"This pre transformation handler has no context and was not executed by the evaluation file");
		}

	}

	private void addLatencyOperators(ILogicalOperator logicalPlan, ISession caller, EvaluationRun run) {
		if (logicalPlan instanceof TopAO) {
			List<ILogicalOperator> newChilds = new ArrayList<>();
			for (LogicalSubscription subscription : logicalPlan.getSubscribedToSource()) {
				ILogicalOperator root = subscription.getTarget();
				CalcLatencyAO latency = new CalcLatencyAO();
				latency.subscribeToSource(root, 0, 0, root.getOutputSchema());
				MapAO map = new MapAO();
				ArrayList<NamedExpression> expressions = new ArrayList<>();
				SDFAttribute latencyAttribute = Latency.schema.get(0).get(Latency.LATENCY_ATTRIBUTE_INDEX);
				expressions.add(new NamedExpression(latencyAttribute.getAttributeName(),
						new SDFExpression(latencyAttribute.getAttributeName(),
								new DirectAttributeResolver(latency.getOutputSchema()), MEP.getInstance()),
						latencyAttribute.getDatatype()));
				map.setExpressions(expressions);
				map.subscribeToSource(latency, 0, 0, latency.getOutputSchema());
				CSVFileSink fileAO = new CSVFileSink();
				fileAO.setWriteMetaData(false);
				fileAO.setNumberFormatter("##################################");
				fileAO.setFloatingFormatter("##################################");
				fileAO.setDelimiter(MeasurementFileUtil.DELIMITER);
				fileAO.setFilename(run.createLatencyResultPath(root));
				fileAO.createDir(true);
				fileAO.subscribeToSource(map, 0, 0, map.getOutputSchema());
				IDataDictionaryWritable dd = (IDataDictionaryWritable) DataDictionaryProvider
						.getDataDictionary(caller.getTenant());
				dd.addSink(root.getName(), fileAO, caller);
				fileAO.setSink(dd.getResource(root.getName(), caller));

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
		CollectOperatorLogicalGraphVisitor<ILogicalOperator> collVisitor = new CollectOperatorLogicalGraphVisitor<>(set,
				true);
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

	private void addResourceOperators(ILogicalOperator logicalPlan, ISession caller, EvaluationRun run) {
		if (logicalPlan instanceof TopAO) {
			List<ILogicalOperator> newChilds = new ArrayList<>();
			for (LogicalSubscription subscription : logicalPlan.getSubscribedToSource()) {
				ILogicalOperator root = subscription.getTarget();
				if (root instanceof CSVFileSink) {
					root = root.getSubscribedToSource(0).getTarget();
				}
				if (root instanceof CalcLatencyAO) {
					root = root.getSubscribedToSource(0).getTarget();
				}
				SystemLoadToPayloadAO systemload = new SystemLoadToPayloadAO();
				systemload.setLoadname("local");
				systemload.setAppend2(false);
				systemload.subscribeToSource(root, 0, 0, root.getOutputSchema());

				ProjectAO projectCPU = new ProjectAO();
				List<SDFAttribute> schemaCPU = new ArrayList<>();
				schemaCPU.add(systemload.getOutputSchema().get(0));
				projectCPU.setOutputSchemaWithList(schemaCPU);
				projectCPU.subscribeToSource(systemload, 0, 0, systemload.getOutputSchema());
				
				CSVFileSink fileCPU = new CSVFileSink();
				fileCPU.setWriteMetaData(false);
				fileCPU.setNumberFormatter("##################################");
				fileCPU.setFloatingFormatter("##################################");
				fileCPU.setDelimiter(MeasurementFileUtil.DELIMITER);
				fileCPU.setFilename(run.createCPUResultPath(fileCPU));
				fileCPU.createDir(true);
				fileCPU.subscribeToSource(root, 0, 0, root.getOutputSchema());
				IDataDictionaryWritable dd = (IDataDictionaryWritable) DataDictionaryProvider
						.getDataDictionary(caller.getTenant());
				dd.addSink(root.getName() + "_CPU", fileCPU, caller);
				fileCPU.setSink(dd.getResource(root.getName() + "_CPU", caller));

				newChilds.add(fileCPU);

				ProjectAO projectMemory = new ProjectAO();
				List<SDFAttribute> schemaMemory = new ArrayList<>();
				schemaMemory.add(systemload.getOutputSchema().get(1));
				projectMemory.setOutputSchemaWithList(schemaMemory);
				projectMemory.subscribeToSource(systemload, 0, 0, systemload.getOutputSchema());
				
				CSVFileSink fileMemory = new CSVFileSink();
				fileMemory.setWriteMetaData(false);
				fileMemory.setNumberFormatter("##################################");
				fileMemory.setFloatingFormatter("##################################");
				fileMemory.setDelimiter(MeasurementFileUtil.DELIMITER);
				fileMemory.setFilename(run.createMemoryResultPath(root));
				fileMemory.createDir(true);
				fileMemory.subscribeToSource(projectMemory, 0, 0, projectMemory.getOutputSchema());
				dd.addSink(root.getName() + "_Memory", fileMemory, caller);
				fileMemory.setSink(dd.getResource(root.getName() + "_Memory", caller));

				newChilds.add(fileMemory);
			}
			logicalPlan.unsubscribeFromAllSources();
			int inputPort = 0;
			for (ILogicalOperator newChild : newChilds) {
				logicalPlan.subscribeToSource(newChild, inputPort++, 0, newChild.getOutputSchema());
			}
		}
	}

}
