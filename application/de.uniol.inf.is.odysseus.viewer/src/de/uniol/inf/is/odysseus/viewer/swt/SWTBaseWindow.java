package de.uniol.inf.is.odysseus.viewer.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.ToolBar;

public class SWTBaseWindow implements ISWTBaseWindow {

	private static SWTBaseWindow instance;
	private static final String SHELL_TITLE = "ODYSSEUS - Query Plan Viewer";
	private static final int SHELL_SIZE = 800;
	
	private Shell shell;
	private Menu menu;
	private ToolBar toolBar;
	private TabFolder tabFolder;
	
	private SWTBaseWindow() {
		shell = new Shell(Display.getDefault());
		shell.setText(SHELL_TITLE);
		shell.setSize(SHELL_SIZE, SHELL_SIZE);
		
		menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		shell.setLayout(new GridLayout());
		
		toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		
		tabFolder = new TabFolder(shell, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	public static SWTBaseWindow getInstance() {
		if( instance == null )
			instance = new SWTBaseWindow();
		return instance;
	}
	
	@Override
	public Shell getShell() {
		return shell;
	}
	
	@Override
	public Menu getMenu() {
		return menu;
	}

	@Override
	public ToolBar getToolBar() {
		return toolBar;
	}

	@Override
	public TabFolder getTabFolder() {
		return tabFolder;
	}
}
