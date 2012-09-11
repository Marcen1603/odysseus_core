package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import org.eclipse.core.expressions.Expression;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;

public class DashboardMenuFactory extends ExtensionContributionFactory {

	@Override
	public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot additions) {
		CommandContributionItemParameter p = new CommandContributionItemParameter(serviceLocator, "", DashboardPlugIn.ADD_DASHBOARD_PART_COMMAND_ID, SWT.PUSH);
		
		IContributionItem item = new CommandContributionItem(p);
		item.setVisible(true);
		
		additions.addContributionItem(item, (Expression)null);
	}

}
