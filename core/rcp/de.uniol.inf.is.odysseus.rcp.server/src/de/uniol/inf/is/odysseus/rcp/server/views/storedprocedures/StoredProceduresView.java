package de.uniol.inf.is.odysseus.rcp.server.views.storedprocedures;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class StoredProceduresView extends ViewPart implements IDataDictionaryListener {

	private static final Logger LOG = LoggerFactory.getLogger(StoredProceduresView.class);

	private TreeViewer viewer;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());

		setTreeViewer(new TreeViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI));
		getTreeViewer().setContentProvider(new StoredProceduresViewContentProvider());
		getTreeViewer().setLabelProvider(new StoredProceduresViewLabelProvider());
		refresh();
		if (OdysseusRCPPlugIn.getExecutor() instanceof IServerExecutor) {
			((IServerExecutor) OdysseusRCPPlugIn.getExecutor()).getDataDictionary().addListener(this);
		}
		// UserManagement.getInstance().addUserManagementListener(this);
		getSite().setSelectionProvider(getTreeViewer());

		// Contextmenu
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(getTreeViewer().getControl());
		// Set the MenuManager
		getTreeViewer().getControl().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, getTreeViewer());

	}

	@Override
	public void dispose() {
		if (OdysseusRCPPlugIn.getExecutor() instanceof IServerExecutor) {
			((IServerExecutor) OdysseusRCPPlugIn.getExecutor()).getDataDictionary().removeListener(this);
		}
		super.dispose();
	}

	@Override
	public void setFocus() {
		getTreeViewer().getControl().setFocus();
	}

	public TreeViewer getTreeViewer() {
		return viewer;
	}

	public void refresh() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					getTreeViewer().setInput(OdysseusRCPPlugIn.getExecutor().getStoredProcedures(OdysseusRCPPlugIn.getActiveSession()));
				} catch (Exception e) {
					LOG.error("Exception during setting input for treeViewer in stored procedures view", e);
				}
			}

		});
	}

	protected void setTreeViewer(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		refresh();
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		refresh();
	}	

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		refresh();
	}

}
