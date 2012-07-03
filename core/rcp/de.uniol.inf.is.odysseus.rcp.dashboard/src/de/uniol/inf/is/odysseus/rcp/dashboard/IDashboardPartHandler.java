package de.uniol.inf.is.odysseus.rcp.dashboard;

import org.eclipse.core.resources.IFile;

public interface IDashboardPartHandler {

	public void save( IDashboardPart part, IFile to);
	public IDashboardPart load( IFile from);
	
}
