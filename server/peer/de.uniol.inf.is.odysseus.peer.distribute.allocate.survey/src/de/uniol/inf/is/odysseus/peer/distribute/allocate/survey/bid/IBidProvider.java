package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;

public interface IBidProvider extends INamedInterface {

	public double calculateBid( ILogicalQuery query, String configName );
	
}
