package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.io.FileNotFoundException;

import org.eclipse.core.resources.IFile;

public interface IDashboardPartHandler {

	public void save( IDashboardPart part, IFile to) throws DashboardHandlerException;
	public IDashboardPart load( IFile from) throws DashboardHandlerException, FileNotFoundException;
	
}
