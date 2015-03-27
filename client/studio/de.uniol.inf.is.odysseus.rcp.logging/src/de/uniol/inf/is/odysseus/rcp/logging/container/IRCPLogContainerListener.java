package de.uniol.inf.is.odysseus.rcp.logging.container;

import de.uniol.inf.is.odysseus.rcp.logging.RCPLogEntry;

public interface IRCPLogContainerListener {

	public void logAdded(RCPLogContainer sender, RCPLogEntry newLogEntry);
	public void logRemoved( RCPLogContainer sender, RCPLogEntry oldLogEntry);
	
}
