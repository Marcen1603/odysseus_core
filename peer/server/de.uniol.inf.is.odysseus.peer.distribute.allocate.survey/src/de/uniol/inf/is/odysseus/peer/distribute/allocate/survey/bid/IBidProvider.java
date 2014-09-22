package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;

public interface IBidProvider extends INamedInterface {

	public Optional<Double> calculateBid( ILogicalQuery query, String configName );
	
}
