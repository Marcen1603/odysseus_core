package de.uniol.inf.is.odysseus.rcp.sources.view.part;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.usermanagement.IUserManagementListener;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class SourcesViewPart extends ViewPart implements
		IDataDictionaryListener, IUserManagementListener {

	private TreeViewer viewer;

	public SourcesViewPart() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());

		setTreeViewer(new TreeViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.SINGLE));
		getTreeViewer().setContentProvider(new SourcesContentProvider());
		getTreeViewer().setLabelProvider(new SourcesLabelProvider());
		refresh();
		getDataDictionary().addListener(this);
		UserManagement.getInstance().addUserManagementListener(this);
		getSite().setSelectionProvider(getTreeViewer());
	}

	@Override
	public void dispose() {
		getDataDictionary().removeListener(this);
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
					getTreeViewer().setInput(
							getDataDictionary().getStreamsAndViews(
									GlobalState.getActiveUser()));
				} catch (Exception e) {
					getTreeViewer().setInput("NOTHING");
					e.printStackTrace();// ?
				}
			}

		});
	}

	public IDataDictionary getDataDictionary() {
		return GlobalState.getActiveDatadictionary();
	}
	

	protected void setTreeViewer(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void addedViewDefinition(DataDictionary sender, String name,
			ILogicalOperator op) {
		refresh();
	}

	@Override
	public void removedViewDefinition(DataDictionary sender, String name,
			ILogicalOperator op) {
		refresh();
	}

	@Override
	public void usersChangedEvent() {
		refresh();
	}

	@Override
	public void roleChangedEvent() {
		refresh();		
	}


}
