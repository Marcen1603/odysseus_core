package de.uniol.inf.is.odysseus.rcp.evaluation.execution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.datadictionary.AbstractDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractSenderAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSink;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MetadataAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.AbstractPreTransformationHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.CollectOperatorLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.latency.Latency;
import de.uniol.inf.is.odysseus.logicaloperator.latency.CalcLatencyAO;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationModel;
import de.uniol.inf.is.odysseus.rcp.evaluation.plot.MeasurementFileUtil;
import de.uniol.inf.is.odysseus.rcp.evaluation.processing.logicaloperator.MeasureThroughputAO;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;
import de.uniol.inf.is.odysseus.systemload.SystemLoad;
import de.uniol.inf.is.odysseus.systemload.logicaloperator.SystemLoadAO;

public class EvaluationPreTransformationHandler extends AbstractPreTransformationHandler {

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
			// Add metadata operator for all metadata that is needed and not
			// available
			addMetadataOperator(query.getLogicalPlan(), caller, run);

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

	private void addMetadataOperator(ILogicalOperator logicalPlan, ISession caller, EvaluationRun run) {
		SortedSet<String> types = new TreeSet<>();
		EvaluationModel model = run.getContext().getModel();
		types.addAll(logicalPlan.getOutputSchema().getMetaAttributeNames());
		if (model.isWithLatency()) {
			types.add(ILatency.class.getName());
		}
		if (model.isWithResource()) {
			types.add(ISystemLoad.class.getName());
		}

		IMetaAttribute metaAttribute = MetadataRegistry.getMetadataType(types);
		// find all accessao and set metadata
		List<ILogicalOperator> sources = AbstractDataDictionary.findSources(logicalPlan);

		for (ILogicalOperator o : sources) {
			MetadataAO toInsert = new MetadataAO();
			toInsert.setLocalMetaAttribute(metaAttribute);
			RestructHelper.insertOperatorBefore(toInsert, o);
		}

		// update schemata in subscriptions!!

		//dumpPlan(logicalPlan);
	}

	@SuppressWarnings("unused")
	private void dumpPlan(ILogicalOperator logicalPlan) {
		SimplePlanPrinter<ILogicalOperator> planPrinter = new SimplePlanPrinter<ILogicalOperator>(true);
		System.err.println(planPrinter.createString(logicalPlan));
	}

	private static MapAO createMapOperator(String expressioName, String expression, ILogicalOperator source) {
		MapAO map = new MapAO();
		ArrayList<NamedExpression> expressions = new ArrayList<>();
		SDFExpression sdfExpression = new SDFExpression(expression,
				new DirectAttributeResolver(source.getOutputSchema()), MEP.getInstance());
		expressions.add(new NamedExpression(expressioName, sdfExpression));
		map.setExpressions(expressions);
		map.subscribeToSource(source, 0, 0, source.getOutputSchema());
		return map;
	}

	private static String createSinkName(String base) {
		return base.replace('.', '_');
	}

	private void addLatencyOperators(ILogicalOperator logicalPlan, ISession caller, EvaluationRun run) {
		if (logicalPlan instanceof TopAO) {

			//dumpPlan(logicalPlan);

			boolean usMaxLatency = run.getContext().getModel().isUseMaxLatency();
			List<ILogicalOperator> newChilds = new ArrayList<>();
			for (LogicalSubscription subscription : logicalPlan.getSubscribedToSource()) {
				ILogicalOperator root = subscription.getTarget();
				if (root instanceof CSVFileSink || root instanceof AbstractSenderAO) {
					root = root.getSubscribedToSource(0).getTarget();
				}
				CalcLatencyAO latency = new CalcLatencyAO();
				latency.subscribeToSource(root, 0, 0, root.getOutputSchema());
				final String expressionName = "Latency";
				final String expression;

				if (usMaxLatency) {
					expression = Latency.schema.get(0).getAttribute(2) + "-" + Latency.schema.get(0).getAttribute(0);
				} else {
					expression = Latency.schema.get(0).getAttribute(3).getAttributeName();
				}
				MapAO latencyOnly = createMapOperator(expressionName, expression, latency);
				CSVFileSink fileAO = new CSVFileSink();
				fileAO.setWriteMetaData(false);
				fileAO.setNumberFormatter("##################################");
				fileAO.setFloatingFormatter("##################################");
				fileAO.setDelimiter(MeasurementFileUtil.DELIMITER);
				fileAO.setFilename(run.createLatencyResultPath(root));
				fileAO.createDir(true);
				fileAO.subscribeToSource(latencyOnly, 0, 0, latencyOnly.getOutputSchema());
				IDataDictionaryWritable dd = (IDataDictionaryWritable) DataDictionaryProvider
						.getDataDictionary(caller.getTenant());
				String sinkName = createSinkName(run.createLatencyResultPath(root));
				dd.addSink(sinkName, fileAO, caller);
				fileAO.setSink(dd.getResource(sinkName, caller));

				newChilds.add(fileAO);
			}
			int inputPort = logicalPlan.getSubscribedToSource().size();
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

	private static MapAO createMapOperatorForSystemLoadAttribute(String attribute_str, int attribute_pos,
			ILogicalOperator source) {
		MapAO map = new MapAO();
		ArrayList<NamedExpression> expressions = new ArrayList<>();
		expressions
				.add(new NamedExpression(attribute_str,
						new SDFExpression("elementAt(EntryList[0], " + attribute_pos + ")",
								new DirectAttributeResolver(source.getOutputSchema()), MEP.getInstance()),
						SDFDatatype.DOUBLE));
		map.setExpressions(expressions);
		map.subscribeToSource(source, 0, 0, source.getOutputSchema());
		return map;
	}

	private void addResourceOperators(ILogicalOperator logicalPlan, ISession caller, EvaluationRun run) {
		if (logicalPlan instanceof TopAO) {
			List<ILogicalOperator> newChilds = new ArrayList<>();
			for (LogicalSubscription subscription : logicalPlan.getSubscribedToSource()) {
				ILogicalOperator root = subscription.getTarget();
				if (root instanceof CSVFileSink || root instanceof AbstractSenderAO) {
					root = root.getSubscribedToSource(0).getTarget();
				}
				if (root instanceof CalcLatencyAO) {
					root = root.getSubscribedToSource(0).getTarget();
				}
				SystemLoadAO systemload = new SystemLoadAO();
				systemload.subscribeToSource(root, 0, 0, root.getOutputSchema());
				MapAO sysloadOnly = createMapOperator(SystemLoad.schema.get(0).getAttribute(0).getAttributeName(),
						SystemLoad.schema.get(0).getAttribute(0).getAttributeName(), systemload);

				MapAO cpuOnly = createMapOperatorForSystemLoadAttribute("cpu", 1, sysloadOnly);
				CSVFileSink fileCPU = new CSVFileSink();
				fileCPU.setWriteMetaData(false);
				fileCPU.setNumberFormatter("##################################");
				fileCPU.setFloatingFormatter("##################################");
				fileCPU.setDelimiter(MeasurementFileUtil.DELIMITER);
				fileCPU.setFilename(run.createCPUResultPath(fileCPU));
				fileCPU.createDir(true);
				fileCPU.subscribeToSource(cpuOnly, 0, 0, cpuOnly.getOutputSchema());
				IDataDictionaryWritable dd = (IDataDictionaryWritable) DataDictionaryProvider
						.getDataDictionary(caller.getTenant());
				String cpuSinkName = createSinkName(run.createCPUResultPath(root));
				dd.addSink(cpuSinkName, fileCPU, caller);
				fileCPU.setSink(dd.getResource(cpuSinkName, caller));

				newChilds.add(fileCPU);

				MapAO memOnly = createMapOperatorForSystemLoadAttribute("memory", 2, sysloadOnly);
				CSVFileSink fileMemory = new CSVFileSink();
				fileMemory.setWriteMetaData(false);
				fileMemory.setNumberFormatter("##################################");
				fileMemory.setFloatingFormatter("##################################");
				fileMemory.setDelimiter(MeasurementFileUtil.DELIMITER);
				fileMemory.setFilename(run.createMemoryResultPath(root));
				fileMemory.createDir(true);
				fileMemory.subscribeToSource(memOnly, 0, 0, memOnly.getOutputSchema());
				String MemSinkName = createSinkName(run.createMemoryResultPath(root));
				dd.addSink(MemSinkName, fileMemory, caller);
				fileMemory.setSink(dd.getResource(MemSinkName, caller));

				newChilds.add(fileMemory);
			}
			int inputPort = logicalPlan.getSubscribedToSource().size();
			for (ILogicalOperator newChild : newChilds) {
				logicalPlan.subscribeToSource(newChild, inputPort++, 0, newChild.getOutputSchema());
			}
		}
	}

}
