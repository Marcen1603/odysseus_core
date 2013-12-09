package de.uniol.inf.is.odysseus.peer.distribute.check;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.DistributionCheckException;
import de.uniol.inf.is.odysseus.peer.distribute.IDistributionChecker;
import de.uniol.inf.is.odysseus.peer.distribute.service.P2PDictionaryService;

public class PeerExistsCheck implements IDistributionChecker {

	private static final Logger LOG = LoggerFactory.getLogger(PeerExistsCheck.class);

	@Override
	public String getName() {
		return "peerExisting";
	}
	
	@Override
	public void check(Collection<ILogicalOperator> operatorss, QueryBuildConfiguration config) throws DistributionCheckException {
		int remotePeersCount = P2PDictionaryService.get().getRemotePeerIDs().size();
		if( remotePeersCount == 0 ) {
			throw new DistributionCheckException("No remote peers known");
		}
		
		LOG.debug("There are {} remote peers which can be used for distribution.", remotePeersCount );
	}
}
