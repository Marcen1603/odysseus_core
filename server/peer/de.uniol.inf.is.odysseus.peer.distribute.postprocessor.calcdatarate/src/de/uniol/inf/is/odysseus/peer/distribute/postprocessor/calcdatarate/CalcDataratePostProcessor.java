package de.uniol.inf.is.odysseus.peer.distribute.postprocessor.calcdatarate;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.datarate.logicaloperator.DatarateAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.peer.distribute.QueryDistributionPostProcessorException;


public class CalcDataratePostProcessor implements IQueryDistributionPostProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(CalcDataratePostProcessor.class);
	
	@Override
	public String getName() {
		return "CalcDatarate";
	}

	@Override
	public void postProcess(IServerExecutor serverExecutor, ISession caller, Map<ILogicalQueryPart, PeerID> allocationMap, ILogicalQuery query, QueryBuildConfiguration config, List<String> parameters) throws QueryDistributionPostProcessorException {
		
		if( parameters.size() != 1 ) {
			throw new QueryDistributionPostProcessorException("CALCDATARATE needs exactly one paramater!");
		}
		
		int updateRate = 0;
		try {
			updateRate = Integer.valueOf(parameters.get(0));
		} catch( Throwable t ) {
			throw new QueryDistributionPostProcessorException("Paramater '" + parameters.get(0) + "' is not valid for CalcDatarate. Integer needed.");
		}
		
		LOG.debug("Beginning post processing calc datarate");
		
		for(ILogicalQueryPart queryPart : allocationMap.keySet()) {
			
			for( ILogicalOperator operator : queryPart.getOperators() ) {
				if( operator.isSourceOperator() && !operator.isSinkOperator() && !(operator instanceof JxtaReceiverPO)) {
					LOG.debug("Found operator {} for inserting DatarateAO after it", operator);
					
					DatarateAO datarateAO = new DatarateAO();
					datarateAO.setUpdateRate(updateRate);
					insertOperator( operator, datarateAO );
					queryPart.addOperator(datarateAO);
				}
			}
		}
	}

	private static void insertOperator(ILogicalOperator op, DatarateAO datarateAO) {
		for(LogicalSubscription subToSink : op.getSubscriptions().toArray(new LogicalSubscription[0])) {
			datarateAO.setOutputSchema(op.getOutputSchema());
			ILogicalOperator sink = subToSink.getTarget();
			
			op.unsubscribeSink(subToSink);
			op.subscribeSink(datarateAO, 0, subToSink.getSourceOutPort(), subToSink.getSchema());
			datarateAO.subscribeSink(sink, subToSink.getSinkInPort(), 0, datarateAO.getOutputSchema());
			datarateAO.initialize();
		}
	}

}
