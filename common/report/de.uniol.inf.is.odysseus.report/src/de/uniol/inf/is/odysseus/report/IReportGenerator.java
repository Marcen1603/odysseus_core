package de.uniol.inf.is.odysseus.report;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public interface IReportGenerator {

	public IReport generateReport( ISession session );
	public IReport generateReport( ISession session, Throwable exception );
	
}
