package de.uniol.inf.is.odysseus.rcp.viewer.graphoutline;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IViewPart;

public interface INodeViewPart extends IViewPart {

	public TreeViewer getTreeViewer();
	public void setSync( boolean sync );
	public boolean getSync();
	public MenuManager getContextMenu();
}
