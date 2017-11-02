package de.uniol.inf.is.odysseus.rcp.evaluation.execution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractSenderAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSink;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MetadataAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ToTupleAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.RenameAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.AbstractPreTransformationHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.CollectOperatorLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.util.GenericGraphWalker;
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
	
	Logger LOG = LoggerFactory.getLogger(EvaluationPreTransformationHandler.class);	
	
	private List<ILogicalOperator> manuallyAdded;
	
	@Override
	public String getName() {
		return "EvaluationPreTransformation";
	}

	@Override
	public void preTransform(IServerExecutor executor, ISession caller, ILogicalQuery query,
			QueryBuildConfiguration config, List<Pair<String, String>> handlerParameters, Context context) {
		Object modelObject = context.get(EvaluationRun.class.getName());
		EvaluationRun run = (EvaluationRun) modelObject;
		this.manuallyAdded = new ArrayList<>();
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

	private void addMetadataOperator(ILogicalPlan logicalPlan, ISession caller, EvaluationRun run) {
		SortedSet<String> types = new TreeSet<>();
		EvaluationModel model = run.getContext().getModel();
		types.addAll(logicalPlan.getOutputSchema().getMetaAttributeNames());
		boolean added = false;
		if (model.isWithLatency() && !types.contains(ILatency.class.getName())) {
			types.add(ILatency.class.getName());
			added = true;
		}
		if (model.isWithResource() && !types.contains(ISystemLoad.class.getName())) {
			types.add(ISystemLoad.class.getName());
			added = true;
		}

		if (added) {

			//LOG.warn("ADDED METADATA WILL CURRENTLY NOT WORK FOR ALL CASES! In case of transformation error, please insert correct metadata (e.g. Systemload) manually"); 
			
			IMetaAttribute metaAttribute = MetadataRegistry.getMetadataType(types);
			// find all accessao and set metadata
			List<ILogicalOperator> sources = logicalPlan.getSources();

			for (ILogicalOperator o : sources) {
				MetadataAO toInsert = new MetadataAO();
				toInsert.setLocalMetaAttribute(metaAttribute);
				// TODO: Check why insertOperatorBefore seems to be right in
				// other cases
				LogicalPlan.insertOperatorBefore2(toInsert, o);
				LogicalPlan.recalcOutputSchemas(toInsert);
			}
		}
		//System.err.println(logicalPlan.getPlanAsString(true));
	}

	private static MapAO createMapAndTupleOperator(String expressionName, String expression, ILogicalOperator source) {
		// Need to convert to tuples for csv file sink
		ToTupleAO tupleAO = createTupleAO(source);

		MapAO map = new MapAO();
		ArrayList<NamedExpression> expressions = new ArrayList<>();
		SDFExpression sdfExpression = new SDFExpression(expression,
				new DirectAttributeResolver(source.getOutputSchema()), MEP.getInstance());
		expressions.add(new NamedExpression(expressionName, sdfExpression));
		map.setExpressions(expressions);
		map.subscribeToSource(tupleAO, 0, 0, tupleAO.getOutputSchema());


		return map;
	}

	private static ToTupleAO createTupleAO(ILogicalOperator source) {
		ToTupleAO tupleAO = new ToTupleAO();
		List<RenameAttribute> attributes = new ArrayList<>();
		// We only need the meta attributes, so remove all attributes from input object
		tupleAO.setAttributes(attributes);
		tupleAO.subscribeToSource(source, 0, 0, source.getOutputSchema());
		return tupleAO;
	}

	private static MapAO createMapOperatorForSystemLoadAttribute(String attribute_str, int attribute_pos,
			ILogicalOperator source) {

		MapAO map = new MapAO();
		SDFExpression sdfExpression = new SDFExpression("elementAt(EntryList[0], " + attribute_pos + ")",
				new DirectAttributeResolver(source.getOutputSchema()), MEP.getInstance());
		SDFDatatype attribType = SDFDatatype.DOUBLE;
		ArrayList<NamedExpression> expressions = new ArrayList<>();
		expressions
				.add(new NamedExpression(attribute_str,
						sdfExpression,
						attribType));
		map.setExpressions(expressions);
		map.subscribeToSource(source, 0, 0, source.getOutputSchema());

		return map;
	}

	private static String createSinkName(String base) {
		return base.replace('.', '_');
	}

	private void addLatencyOperators(ILogicalPlan logicalPlan, ISession caller, EvaluationRun run) {
		ILogicalOperator planRoot = logicalPlan.getRoot();

		if (planRoot instanceof TopAO) {

			// dumpPlan(logicalPlan);
			boolean usMaxLatency = run.getContext().getModel().isUseMaxLatency();
			List<ILogicalOperator> newChilds = new ArrayList<>();
			for (LogicalSubscription subscription : planRoot.getSubscribedToSource()) {
				ILogicalOperator root = subscription.getSource();
				
				// Check, if we added the operator here to avoid adding sinks twice
				if (this.manuallyAdded.contains(root)) {
					continue;
				}
				
				if (root instanceof CSVFileSink || root instanceof AbstractSenderAO) {
					root = root.getSubscribedToSource(0).getSource();
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
				MapAO latencyOnly = createMapAndTupleOperator(expressionName, expression, latency);

				CSVFileSink fileAO = new CSVFileSink();
				fileAO.setWriteMetaData(false);
				fileAO.setNumberFormatter("##################################");
				fileAO.setFloatingFormatter("##################################");
				fileAO.setDelimiter(MeasurementFileUtil.DELIMITER);
				fileAO.setFilename(run.createLatencyResultPath(root));
				fileAO.createDir(true);
				fileAO.subscribeToSource(latencyOnly, 0, 0, latencyOnly.getOutputSchema());
				IDataDictionaryWritable dd = (IDataDictionaryWritable) DataDictionaryProvider.instance
						.getDataDictionary(caller.getTenant());
				String sinkName = createSinkName(run.createLatencyResultPath(root));
				dd.addSink(sinkName, new LogicalPlan(fileAO), caller);
				fileAO.setSink(dd.getResource(sinkName, caller));

				newChilds.add(fileAO);
			}
			this.manuallyAdded.addAll(newChilds);
			int inputPort = planRoot.getSubscribedToSource().size();
			for (ILogicalOperator newChild : newChilds) {
				planRoot.subscribeToSource(newChild, inputPort++, 0, newChild.getOutputSchema());
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addThroughputOperators(ILogicalPlan plan, ISession caller, EvaluationRun run) {

		Set<Class<? extends ILogicalOperator>> set = new HashSet<>();
		set.add(AbstractAccessAO.class);
		set.add(StreamAO.class);
		CollectOperatorLogicalGraphVisitor<ILogicalOperator> collVisitor = new CollectOperatorLogicalGraphVisitor<>(set,
				true);
		GenericGraphWalker collectWalker = new GenericGraphWalker();
		collectWalker.prefixWalk(plan.getRoot(), collVisitor);
		for (ILogicalOperator accessAO : collVisitor.getResult()) {

			List<LogicalSubscription> nextSinks = new ArrayList<>(accessAO.getSubscriptions());
			accessAO.unsubscribeFromAllSinks();
			MeasureThroughputAO mt = new MeasureThroughputAO();
			mt.setEach(run.getContext().getModel().getMeasureThrougputEach());
			mt.subscribeToSource(accessAO, 0, 0, accessAO.getOutputSchema());
			mt.setFilename(run.createThroughputResultPath(mt.getInputAO()));
			for (LogicalSubscription sub : nextSinks) {
				mt.subscribeSink(sub.getSink(), sub.getSinkInPort(), sub.getSourceOutPort(), mt.getOutputSchema());
			}
		}
	}



	private void addResourceOperators(ILogicalPlan logicalPlan, ISession caller, EvaluationRun run) {
		ILogicalOperator planRoot = logicalPlan.getRoot();

		if (planRoot instanceof TopAO) {
			List<ILogicalOperator> newChilds = new ArrayList<>();
			for (LogicalSubscription subscription : planRoot.getSubscribedToSource()) {
				ILogicalOperator root = subscription.getSource();
				
				// Check, if we added the operator here to avoid adding sinks twice
				if (this.manuallyAdded.contains(root)) {
					continue;
				}
				
				if (root instanceof CSVFileSink || root instanceof AbstractSenderAO) {
					root = root.getSubscribedToSource(0).getSource();
				}
				if (root instanceof CalcLatencyAO) {
					root = root.getSubscribedToSource(0).getSource();
				}
				SystemLoadAO systemload = new SystemLoadAO();
				systemload.subscribeToSource(root, 0, 0, root.getOutputSchema());

				MapAO sysloadOnly = createMapAndTupleOperator(SystemLoad.schema.get(0).getAttribute(0).getAttributeName(),
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
				IDataDictionaryWritable dd = (IDataDictionaryWritable) DataDictionaryProvider.instance
						.getDataDictionary(caller.getTenant());
				String cpuSinkName = createSinkName(run.createCPUResultPath(root));
				dd.addSink(cpuSinkName, new LogicalPlan(fileCPU), caller);
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
				dd.addSink(MemSinkName, new LogicalPlan(fileMemory), caller);
				fileMemory.setSink(dd.getResource(MemSinkName, caller));

				newChilds.add(fileMemory);
			}
			this.manuallyAdded.addAll(newChilds);
			int inputPort = planRoot.getSubscribedToSource().size();
			for (ILogicalOperator newChild : newChilds) {
				planRoot.subscribeToSource(newChild, inputPort++, 0, newChild.getOutputSchema());
			}
		}
	}

}
