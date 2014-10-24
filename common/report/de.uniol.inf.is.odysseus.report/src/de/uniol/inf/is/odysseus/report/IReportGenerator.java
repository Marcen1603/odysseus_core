package de.uniol.inf.is.odysseus.report;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public interface IReportGenerator {

	public String generateReport( ISession session );
	public String generateReport( ISession session, Throwable exception );
	
}
