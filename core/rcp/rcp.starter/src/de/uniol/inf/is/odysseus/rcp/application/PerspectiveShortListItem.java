package de.uniol.inf.is.odysseus.rcp.application;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.actions.ContributionItemFactory;

public class PerspectiveShortListItem extends CompoundContributionItem {

	public PerspectiveShortListItem() {
	}

	public PerspectiveShortListItem(String id) {
		super(id);
	}

	@Override
	protected IContributionItem[] getContributionItems() {
		List<IContributionItem> menuContributionList = new ArrayList<IContributionItem>();
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IContributionItem item = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);
		menuContributionList.add(item); // add the list of views in the menu
		return menuContributionList.toArray(new IContributionItem[menuContributionList.size()]);
	}

}
