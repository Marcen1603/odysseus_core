package de.uniol.inf.is.odysseus.rcp.logging.view.filter;

import de.uniol.inf.is.odysseus.rcp.logging.RCPLogEntry;

public class AllRCPLogFilter implements IRCPLogFilter {

	public static final AllRCPLogFilter INSTANCE = new AllRCPLogFilter();
	
	private AllRCPLogFilter() {
	}
	
	@Override
	public boolean isShown(RCPLogEntry entry) {
		return true;
	}

}
