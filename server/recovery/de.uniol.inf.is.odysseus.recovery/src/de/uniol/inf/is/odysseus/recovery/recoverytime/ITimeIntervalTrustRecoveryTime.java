package de.uniol.inf.is.odysseus.recovery.recoverytime;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.trust.ITimeIntervalTrust;
import de.uniol.inf.is.odysseus.trust.ITrust;

/**
 * Combined eta attribute for {@link ITimeInterval}, {@link ITrust} and
 * {@link IRecoveryTime}.
 * 
 * @author Michael Brand
 *
 */
public interface ITimeIntervalTrustRecoveryTime extends ITimeIntervalTrust, IRecoveryTime {

	@Override
	public ITimeIntervalTrustRecoveryTime clone();

}