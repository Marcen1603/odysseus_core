package de.uniol.inf.is.odysseus.p2p_new.distributor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.parser.pql.generator.PQLGenerator;

public class P2PDistributor implements ILogicalQueryDistributor {

	private static final Logger LOG = LoggerFactory.getLogger(P2PDistributor.class);
	
	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender, List<ILogicalQuery> queriesToDistribute) {
		for( ILogicalQuery query : queriesToDistribute ) {
			LOG.debug("\n" + PQLGenerator.generatePQLStatement(query.getLogicalPlan()));
		}
		
		return queriesToDistribute;
	}

	
}
