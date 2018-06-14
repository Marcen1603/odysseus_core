package de.uniol.inf.is.odysseus.net.querydistribute.postprocess.calculations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.logicaloperator.latency.CalcLatencyAO;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryDistributionPostProcessorException;

/**
 * Post processor to insert {@link CalcLatencyAO}s before every sink.
 *
 * @author Michael Brand
 *
 */
public class CalcLatencyPostProcessor implements IQueryDistributionPostProcessor {

	/**
	 * The version of this class for serializations.
	 */
	private static final long serialVersionUID = -5933877618211847595L;

	/**
	 * The name for Odysseus Script.
	 */
	private static final String name = "CalcLatency";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void postProcess(IServerExecutor serverExecutor, ISession caller,
			Map<ILogicalQueryPart, IOdysseusNode> allocationMap, ILogicalQuery query, QueryBuildConfiguration config,
			List<String> parameters) throws QueryDistributionPostProcessorException {
		// Insert calc latency operators
		allocationMap.keySet()
				.forEach(part -> part.getOperators().stream()
						.filter(operator -> operator.isSinkOperator() && !operator.isSourceOperator())
						.forEach(operator -> insertCalcLatencyAO(operator, part)));
		// Insert latency metadata
		MetaDataInserter.insertMetaData(allocationMap.keySet(), ILatency.class);
	}

	/**
	 * Inserts a calc latency operator before a given sink in a given query
	 * part.
	 */
	private static void insertCalcLatencyAO(ILogicalOperator sink, ILogicalQueryPart part) {
		Collection<ILogicalOperator> calcLatencyAOs = new ArrayList<>();
		Collection<LogicalSubscription> subs = new ArrayList<>(sink.getSubscribedToSource());
		sink.unsubscribeFromAllSources();
		subs.stream().forEach(sub -> calcLatencyAOs.add(insertCalcLatencyAO(sink, sub)));
		part.addOperators(calcLatencyAOs);
	}

	/**
	 * Inserts a calc latency operator before a given sink in a given query
	 * part.
	 */
	private static CalcLatencyAO insertCalcLatencyAO(ILogicalOperator sink, LogicalSubscription subToSink) {
		CalcLatencyAO calcLatencyAO = new CalcLatencyAO();
		calcLatencyAO.subscribeToSource(subToSink.getSource(), 0, subToSink.getSourceOutPort(), subToSink.getSchema());
		sink.subscribeToSource(calcLatencyAO, subToSink.getSinkInPort(), 0, subToSink.getSchema());
		return calcLatencyAO;
	}

}