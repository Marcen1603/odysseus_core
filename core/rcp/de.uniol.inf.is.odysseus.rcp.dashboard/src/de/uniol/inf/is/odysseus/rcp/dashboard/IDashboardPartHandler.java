package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.io.IOException;

import org.eclipse.core.resources.IFile;

public interface IDashboardPartHandler {

	public void save( IDashboardPart part, IFile to) throws IOException;
	public IDashboardPart load( IFile from) throws IOException;
	
}
