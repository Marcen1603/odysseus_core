package de.uniol.inf.is.odysseus.rcp.logging.view.filter;

import de.uniol.inf.is.odysseus.rcp.logging.RCPLogEntry;

public interface IRCPLogFilter {

	public boolean isShown( RCPLogEntry entry );
	
}
