package de.uniol.inf.is.odysseus.rcp.logging;

import java.io.IOException;
import java.util.List;

public interface ILogSaver {

	public void save( String filename, List<RCPLogEntry> entriesToSave ) throws IOException;
	
	public String getName();
	public String getFilenameExtension();
	
}
