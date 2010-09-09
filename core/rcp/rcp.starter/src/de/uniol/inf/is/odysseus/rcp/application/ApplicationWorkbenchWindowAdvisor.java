package de.uniol.inf.is.odysseus.rcp.application;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(600, 600));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setTitle("Odysseus RCP");
		configurer.setShowProgressIndicator(true);
		configurer.setShowPerspectiveBar(true);
	}
	
	@Override
	public void postWindowOpen() {
		IStatusLineManager manager = getWindowConfigurer().getActionBarConfigurer().getStatusLineManager();
		StatusBarManager.getInstance().setStatusLineManager(manager);
	}
}
