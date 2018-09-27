package de.uniol.inf.is.odysseus.report;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public interface IReportProvider {

	public int getPriority();
	
	public String getTitle();
	public String getReport(ISession session);
	
}
