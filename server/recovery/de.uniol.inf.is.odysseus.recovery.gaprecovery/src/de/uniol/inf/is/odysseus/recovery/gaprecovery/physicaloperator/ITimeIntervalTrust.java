package de.uniol.inf.is.odysseus.recovery.gaprecovery.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.trust.ITrust;

/**
 * Ensemble needed for physical operators. There, stream objects can only have
 * one super type (generics).
 * 
 * @author Michael Brand
 *
 */
public interface ITimeIntervalTrust extends ITimeInterval, ITrust {

	@Override
	public ITimeIntervalTrust clone();

}