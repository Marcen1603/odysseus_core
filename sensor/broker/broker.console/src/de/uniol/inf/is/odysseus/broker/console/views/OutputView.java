package de.uniol.inf.is.odysseus.broker.console.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.broker.console.IContentListener;
import de.uniol.inf.is.odysseus.broker.console.ViewController;

public class OutputView extends ViewPart implements IContentListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "de.uniol.inf.is.odysseus.broker.console.views.OutputView";

	private Action clearTableAction;
	private Action removeAllQeueriesAction;
	private TabFolder tabFolder;
	private List<OutputTabView> tabViews = new ArrayList<OutputTabView>();

	/**
	 * The constructor.
	 */
	public OutputView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		
		tabFolder = new TabFolder(parent, SWT.BORDER);		
		makeActions();

		contributeToActionBars();
		initTabs();
		ViewController.getInstance().addOutputListener(this);

	}

	public void initTabs() {
		Set<Integer> ports = ViewController.getInstance().getPorts();
		for (Integer port : ports) {
			if (!this.containsTab(port.intValue())) {
				this.addTab(port.intValue());
			}
		}
	}

	public void addTab(int port) {
		System.out.println(this.toString()+"tabFolder is "+tabFolder);
		TabItem tabItem = new TabItem(tabFolder, SWT.BORDER);
		tabItem.setText("Query " + (port + 1));
		String[] names = ViewController.getInstance().getContentProvider(port)
				.getAttributeNames();
		OutputTabView tabView = new OutputTabView(this, port, names);
		this.tabViews.add(tabView);
		tabItem.setControl(tabView.createPartControl(tabFolder));
		tabFolder.setSelection(tabItem);
		setActionsEnable(true);

	}

	protected void clearAll() {
		ViewController.getInstance().clearAll();
		this.tabViews.clear();
		this.setActionsEnable(false);
		for (TabItem item : tabFolder.getItems()) {			
			item.dispose();
		}		


	}

	public void setFocus() {
	}

	public void refreshViewer() {
		for (OutputTabView tabView : this.tabViews) {
			tabView.refreshViewer();
		}
	}
	
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(clearTableAction);
		manager.add(new Separator());
		manager.add(removeAllQeueriesAction);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(clearTableAction);
		manager.add(new Separator());
		manager.add(removeAllQeueriesAction);
	}

	private void makeActions() {
		// clear current table
		clearTableAction = new Action() {
			public void run() {
				int index = OutputView.this.tabFolder.getSelectionIndex();
				if (index >= 0) {
					OutputView.this.tabViews.get(index).clearTable();
				}
			}
		};
		clearTableAction.setText("Clear current table");
		clearTableAction.setToolTipText("Removes all items from current table");
		clearTableAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages().getImageDescriptor(
						ISharedImages.IMG_ETOOL_CLEAR));
		clearTableAction.setDisabledImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages().getImageDescriptor(
						ISharedImages.IMG_ETOOL_CLEAR_DISABLED));

		removeAllQeueriesAction = new Action() {
			public void run() {
				OutputView.this.clearAll();

			}
		};
		removeAllQeueriesAction.setText("Remove all queries");
		removeAllQeueriesAction.setToolTipText("Removes all queries");
		removeAllQeueriesAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages().getImageDescriptor(
						ISharedImages.IMG_ELCL_REMOVEALL));
		removeAllQeueriesAction.setDisabledImageDescriptor(PlatformUI
				.getWorkbench().getSharedImages().getImageDescriptor(
						ISharedImages.IMG_ELCL_REMOVEALL_DISABLED));

		setActionsEnable(false);
	}

	private void setActionsEnable(boolean value) {
		this.clearTableAction.setEnabled(value);
		this.removeAllQeueriesAction.setEnabled(value);
	}

	@Override
	public void newQueryEvent(int port) {
		if (!this.containsTab(port)) {
			this.addTab(port);
		}
	}

	public boolean containsTab(int port) {
		for (OutputTabView tab : this.tabViews) {
			if (tab.getPort() == port) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void inputChanged(int port) {
		for (OutputTabView tab : this.tabViews) {
			if (tab.getPort() == port) {
				tab.refreshViewer();
			}
		}

	}

	public void dispose() {		
		ViewController.getInstance().removeContentListener(this);
		this.tabViews.clear();
		this.setActionsEnable(false);
	}

}