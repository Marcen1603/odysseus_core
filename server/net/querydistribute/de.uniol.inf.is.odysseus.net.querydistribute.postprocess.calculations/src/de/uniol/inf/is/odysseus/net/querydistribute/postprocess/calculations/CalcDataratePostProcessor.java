package de.uniol.inf.is.odysseus.net.querydistribute.postprocess.calculations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.datarate.IDatarate;
import de.uniol.inf.is.odysseus.datarate.logicaloperator.DatarateAO;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryDistributionPostProcessorException;

/**
 * Post processor to insert {@link DatarateAO}s after every source.
 *
 * @author Michael Brand
 *
 */
public class CalcDataratePostProcessor implements IQueryDistributionPostProcessor {

	/**
	 * The version of this class for serializations.
	 */
	private static final long serialVersionUID = -5933877618211847595L;

	/**
	 * The name for Odysseus Script.
	 */
	private static final String name = "CalcDatarate";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void postProcess(IServerExecutor serverExecutor, ISession caller,
			Map<ILogicalQueryPart, IOdysseusNode> allocationMap, ILogicalQuery query, QueryBuildConfiguration config,
			List<String> parameters) throws QueryDistributionPostProcessorException {
		if (parameters.isEmpty()) {
			throw new QueryDistributionPostProcessorException(name + " needs the update rate!");
		}
		final int updateRate = Integer.parseInt(parameters.get(0));

		// Insert datarate operators
		allocationMap.keySet()
				.forEach(part -> part.getOperators().stream()
						.filter(operator -> !operator.isSinkOperator() && operator.isSourceOperator())
						.forEach(operator -> insertCalcDatarateAO(operator, part, updateRate)));
		// Insert datarate metadata
		MetaDataInserter.insertMetaData(allocationMap.keySet(), IDatarate.class);
	}

	/**
	 * Inserts a {@link DatarateAO} after a given source in a given query part.
	 */
	private static void insertCalcDatarateAO(ILogicalOperator source, ILogicalQueryPart part, int updateRate) {
		Collection<ILogicalOperator> calcDatarateAOs = new ArrayList<>();
		Collection<LogicalSubscription> subs = new ArrayList<>(source.getSubscriptions());
		source.unsubscribeFromAllSinks();
		subs.stream().forEach(sub -> calcDatarateAOs.add(insertCalcDatarateAO(source, sub, updateRate)));
		part.addOperators(calcDatarateAOs);
	}

	/**
	 * Inserts a {@link DatarateAO} after a given source in a given query part.
	 */
	private static DatarateAO insertCalcDatarateAO(ILogicalOperator source, LogicalSubscription subFromSource,
			int updateRate) {
		DatarateAO calcDatarateAO = new DatarateAO();
		calcDatarateAO.setUpdateRate(updateRate);
		calcDatarateAO.subscribeSink(subFromSource.getSink(), subFromSource.getSinkInPort(), 0,
				subFromSource.getSchema());
		source.subscribeSink(calcDatarateAO, 0, subFromSource.getSinkInPort(), subFromSource.getSchema());
		return calcDatarateAO;
	}

}