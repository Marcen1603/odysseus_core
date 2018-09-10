package de.uniol.inf.is.odysseus.rcp.logging.view;

import java.util.List;

import de.uniol.inf.is.odysseus.rcp.logging.RCPLogEntry;

public interface ILogTableListener {

	public void selectionChanged( LogTable table, List<RCPLogEntry> selection );
	
}
