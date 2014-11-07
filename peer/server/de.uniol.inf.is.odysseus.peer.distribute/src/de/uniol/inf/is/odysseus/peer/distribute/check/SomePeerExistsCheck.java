package de.uniol.inf.is.odysseus.peer.distribute.check;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.DistributionCheckException;
import de.uniol.inf.is.odysseus.peer.distribute.IDistributionChecker;

public class SomePeerExistsCheck implements IDistributionChecker {

	private static final Logger LOG = LoggerFactory.getLogger(SomePeerExistsCheck.class);

	private static IPeerDictionary peerDictionary;

	// called by OSGi-DS
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if (peerDictionary == serv) {
			peerDictionary = null;
		}
	}
	
	@Override
	public String getName() {
		return "somePeerExists";
	}
	
	@Override
	public void check(Collection<ILogicalOperator> operatorss, ILogicalQuery query, QueryBuildConfiguration config) throws DistributionCheckException {
		int remotePeersCount = peerDictionary.getRemotePeerIDs().size();
		if( remotePeersCount == 0 ) {
			throw new DistributionCheckException("No remote peers known");
		}
		
		LOG.debug("There are {} remote peers which can be used for distribution.", remotePeersCount );
	}
}
