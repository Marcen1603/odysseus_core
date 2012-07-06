package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.io.FileNotFoundException;

import org.eclipse.core.resources.IFile;

import de.uniol.inf.is.odysseus.rcp.dashboard.editors.Dashboard;

public interface IDashboardHandler {

	public Dashboard load( IFile file, IDashboardPartHandler partHandler ) throws DashboardHandlerException, FileNotFoundException;
	public void save( IFile to, Dashboard board) throws DashboardHandlerException;
	
}
