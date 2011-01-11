package de.uniol.inf.is.odysseus.rcp.application;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	@Override
	protected void makeActions(final IWorkbenchWindow window) {
		register( ActionFactory.SAVE.create(window));
		register( ActionFactory.SAVE_ALL.create(window));
		register( ActionFactory.SAVE_AS.create(window));
		register( ActionFactory.REVERT.create(window));
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		
	}

}
