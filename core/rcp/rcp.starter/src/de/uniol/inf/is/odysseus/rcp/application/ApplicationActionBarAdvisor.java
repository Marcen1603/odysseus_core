package de.uniol.inf.is.odysseus.rcp.application;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
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
//	private IContributionItem newShortlistAction;
//	private IWorkbenchAction exitAction;
//	private IWorkbenchAction preferencesAction;
//	private IWorkbenchAction aboutAction;
//	private IWorkbenchAction resetPerspectiveAction;
//	private IWorkbenchAction closePerspectiveAction;
//	private IWorkbenchAction closeAllPerspectivesAction;
//	private IContributionItem viewsShortlistAction;
//	private IContributionItem perspectivesShortlistAction;
//	
//	private IWorkbenchAction undo;
//	private IWorkbenchAction redo;
//	private IWorkbenchAction cut;
//	private IWorkbenchAction copy;
//	private IWorkbenchAction paste;
//	private IWorkbenchAction delete;
//	private IWorkbenchAction selectAll;
//	private IWorkbenchAction find;


	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	@Override
	protected void makeActions(final IWorkbenchWindow window) {
//		newShortlistAction = ContributionItemFactory.NEW_WIZARD_SHORTLIST.create(window);
//		
//		exitAction = ActionFactory.QUIT.create(window);
//		register(exitAction);
//		
//		
//		undo = ActionFactory.UNDO.create(window);
//		register(undo);
//		redo = ActionFactory.REDO.create(window);
//		register(redo);
//		cut = ActionFactory.CUT.create(window);
//		register(cut);
//		copy = ActionFactory.COPY.create(window);
//		register(copy);
//		paste = ActionFactory.PASTE.create(window);
//		register(paste);
//		delete = ActionFactory.DELETE.create(window);
//		register(delete);
//		selectAll = ActionFactory.SELECT_ALL.create(window);
//		register(selectAll);
//		find = ActionFactory.FIND.create(window);
//		register(find);
//		
//		preferencesAction = ActionFactory.PREFERENCES.create(window);
//		register(preferencesAction);
//		
//		aboutAction = ActionFactory.ABOUT.create(window);
//		register(aboutAction);
//		viewsShortlistAction = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
//		perspectivesShortlistAction = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);
//		
//		resetPerspectiveAction = ActionFactory.RESET_PERSPECTIVE.create(window);
//		register(resetPerspectiveAction);
//		closePerspectiveAction = ActionFactory.CLOSE_PERSPECTIVE.create(window);
//		register(closePerspectiveAction);
//		closeAllPerspectivesAction = ActionFactory.CLOSE_ALL_PERSPECTIVES.create(window);
//		register(closeAllPerspectivesAction);
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
//		MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
//		MenuManager newMenu = new MenuManager("New", ActionFactory.NEW.getId());
//		MenuManager editMenu = new MenuManager("&Edit", IWorkbenchActionConstants.M_EDIT);
//		MenuManager windowMenu = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);
//		MenuManager perspectivesMenu = new MenuManager("Open Perspective", "perspectives");
//		MenuManager viewsMenu = new MenuManager("Show View", "views");
//		MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
//		menuBar.add(fileMenu);
//		menuBar.add(editMenu);
//		menuBar.add(windowMenu);
//		menuBar.add(helpMenu);
//		
//		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
//		fileMenu.add(newMenu);
//		newMenu.add(new Separator(ActionFactory.NEW.getId()));
//		newMenu.add(newShortlistAction);
//		newMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
//		fileMenu.add(exitAction);
//		
//		editMenu.add(undo);
//		editMenu.add(redo);
//		editMenu.add(new Separator());
//		editMenu.add(cut);
//		editMenu.add(copy);
//		editMenu.add(paste);
//		editMenu.add(new Separator());
//		editMenu.add(delete);
//		editMenu.add(selectAll);
//		editMenu.add(new Separator("find.ext"));
//		editMenu.add(find);
//		
//		viewsMenu.add(viewsShortlistAction);
//		windowMenu.add(viewsMenu);
//		perspectivesMenu.add(perspectivesShortlistAction);
//		windowMenu.add(perspectivesMenu);
//		windowMenu.add(new Separator());
//		windowMenu.add(resetPerspectiveAction);
//		windowMenu.add(closePerspectiveAction);
//		windowMenu.add(closeAllPerspectivesAction);
//		windowMenu.add(new Separator());
//		windowMenu.add(preferencesAction);
//		
//		
//		helpMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
//		helpMenu.add(new Separator());
//		helpMenu.add(aboutAction);
	}

}
