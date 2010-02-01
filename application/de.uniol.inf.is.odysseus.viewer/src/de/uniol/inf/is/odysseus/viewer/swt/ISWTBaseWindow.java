package de.uniol.inf.is.odysseus.viewer.swt;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.ToolBar;

public interface ISWTBaseWindow {

	public Menu getMenu();
	public Shell getShell();
	public ToolBar getToolBar();
	public TabFolder getTabFolder();
}
