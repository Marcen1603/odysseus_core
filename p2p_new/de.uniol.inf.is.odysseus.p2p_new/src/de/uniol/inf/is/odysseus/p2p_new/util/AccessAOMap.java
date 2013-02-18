package de.uniol.inf.is.odysseus.p2p_new.util;

import java.util.HashMap;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;

public class AccessAOMap extends HashMap<String, AccessAO>{

	private static final long serialVersionUID = -7154169119428278894L;
	
	public AccessAO putIndirect(String key, ILogicalOperator operator) {
		Optional<AccessAO> optAccessAO = determineAccessAO(operator);
		if( optAccessAO.isPresent() ) {
			return put(key, optAccessAO.get());
		} else {
			throw new IllegalArgumentException("AccessAO based on operator '" + operator + "' not found (key = '" + key + "')");
		}
	}

	private static Optional<AccessAO> determineAccessAO(ILogicalOperator start) {
		if( start instanceof AccessAO) {
			return Optional.of((AccessAO)start);
		}
		
		for( LogicalSubscription subscription : start.getSubscribedToSource()) {
			Optional<AccessAO> optAcccessAO = determineAccessAO(subscription.getTarget());
			if( optAcccessAO.isPresent() ) {
				return optAcccessAO;
			}
		}
		
		return Optional.absent();
	}
}
