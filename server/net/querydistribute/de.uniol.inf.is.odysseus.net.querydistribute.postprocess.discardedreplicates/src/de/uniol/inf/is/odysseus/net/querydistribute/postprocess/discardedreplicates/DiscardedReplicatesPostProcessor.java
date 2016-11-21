package de.uniol.inf.is.odysseus.net.querydistribute.postprocess.discardedreplicates;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryDistributionPostProcessorException;
import de.uniol.inf.is.odysseus.server.replication.logicaloperator.ReplicationMergeAO;

/**
 * Post processor to insert a sender for each {@link ReplicationMergeAO}. The
 * sender will be inserted for the output port 1 that is not used normally. All
 * discarded replicates are sent to port 1. The sender writes the data in a CSV
 * file (one file per merger). The only argument for this post processor is the
 * path to the CSV files. The names of the files are determined by the
 * {@link ReplicationMergeAO} (name and hashCode). <br />
 * <br />
 * Used sender settings: <br />
 * - transport handler "file" <br />
 * - data handler "tuple" <br />
 * - wrapper "GenericPush" <br />
 * - protocol handler "csv" <br />
 * - "createDir" "true" <br />
 * - "filename", path_from_user + "/" + merger.getName() + merger.hashCode() +
 * ".csv" <br />
 * - "append", "true" <br />
 * - "writeMetaData", "true
 *
 * @author Michael Brand
 *
 */
public class DiscardedReplicatesPostProcessor implements IQueryDistributionPostProcessor {

	private static final long serialVersionUID = 1670612711118701115L;

	private static final String postProcessorName = "discardedreplicates";

	@Override
	public String getName() {
		return postProcessorName;
	}

	@Override
	public void postProcess(IServerExecutor serverExecutor, ISession caller,
			Map<ILogicalQueryPart, IOdysseusNode> allocationMap, ILogicalQuery query, QueryBuildConfiguration config,
			List<String> parameters) throws QueryDistributionPostProcessorException {
		if (parameters.isEmpty()) {
			throw new QueryDistributionPostProcessorException(
					postProcessorName + " needs a path where to create csv files to store the discarded replicates!");
		}
		final String path = parameters.get(0);
		allocationMap.keySet()
				.forEach(part -> part.getOperators().stream().filter(operator -> operator instanceof ReplicationMergeAO)
						.forEach(operator -> insertSender((ReplicationMergeAO) operator, part, path, caller)));
	}

	private static void insertSender(ReplicationMergeAO merger, ILogicalQueryPart part, String path, ISession caller) {
		SenderAO sender = new SenderAO();
		sender.setTransportHandler("file");
		sender.setDataHandler("tuple");
		sender.setWrapper("GenericPush");
		sender.setProtocolHandler("csv");
		sender.setWriteMetaData(true);
		final OptionMap options = new OptionMap();
		options.setOption("createDir", String.valueOf(true));
		options.setOption("filename", path + "/" + merger.getName() + merger.hashCode() + ".csv");
		options.setOption("append", String.valueOf(true));
		sender.setOptionMap(options);
		sender.setSink(new Resource(caller.getUser(), merger.toString()));
		sender.setName(postProcessorName);
		sender.subscribeToSource(merger, 0, 1, merger.getOutputSchema());
		part.addOperator(sender);
	}

}