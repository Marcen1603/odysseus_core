package de.uniol.inf.is.odysseus.rcp.application;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.
	private IWorkbenchAction newAction;
	private IWorkbenchAction exitAction;
	private IWorkbenchAction preferencesAction;
	private IWorkbenchAction aboutAction;
	private IWorkbenchAction resetPerspectiveAction;
	private IWorkbenchAction closePerspectiveAction;
	private IWorkbenchAction closeAllPerspectivesAction;
	private IContributionItem showViewAction;
	private IContributionItem perspectivesMenuAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(final IWorkbenchWindow window) {
		// Creates the actions and registers them.
		// Registering is needed to ensure that key bindings work.
		// The corresponding commands keybindings are defined in the plugin.xml
		// file.
		// Registering also provides automatic disposal of the actions when
		// the window is closed.

		newAction = ActionFactory.NEW.create(window);
		newAction.setText("New...");
		register(newAction);
		
		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);
		
		preferencesAction = ActionFactory.PREFERENCES.create(window);
		register(preferencesAction);
		
		aboutAction = ActionFactory.ABOUT.create(window);
		showViewAction = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
		perspectivesMenuAction = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);
		
		resetPerspectiveAction = ActionFactory.RESET_PERSPECTIVE.create(window);
		closePerspectiveAction = ActionFactory.CLOSE_PERSPECTIVE.create(window);
		closeAllPerspectivesAction = ActionFactory.CLOSE_ALL_PERSPECTIVES.create(window);
	}

	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
		MenuManager windowMenu = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);
		MenuManager perspectivesMenu = new MenuManager("Open Perspective", "perspectives");
		MenuManager viewsMenu = new MenuManager("Show View", "views");
		MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
		menuBar.add(fileMenu);
		menuBar.add(windowMenu);
		menuBar.add(helpMenu);
		
		fileMenu.add(newAction);
		fileMenu.add(exitAction);
		
		viewsMenu.add(showViewAction);
		windowMenu.add(viewsMenu);
		perspectivesMenu.add(perspectivesMenuAction);
		windowMenu.add(perspectivesMenu);
		windowMenu.add(new Separator());
		windowMenu.add(resetPerspectiveAction);
		windowMenu.add(closePerspectiveAction);
		windowMenu.add(closeAllPerspectivesAction);
		windowMenu.add(new Separator());
		windowMenu.add(preferencesAction);
		
		
		helpMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		helpMenu.add(new Separator());
		helpMenu.add(aboutAction);
	}

}
