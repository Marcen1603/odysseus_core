package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views;

import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;

public class MapConnectionView extends AbstractStreamMapEditorViewPart {

	private static final Logger LOG = LoggerFactory.getLogger(MapConnectionView.class);

	private TreeViewer treeViewer;
	private Action addConnectionAction;
	private Action removeConnectionAction;
	
	
	@Override
	public void setFocus() {

	}

	@Override
	public void updatePartControl(Composite parent) {
		//LOG.debug("Update Connections");
		treeViewer = new TreeViewer(parent);
		treeViewer.setContentProvider(new ConnectionTreeContentProvider());
		treeViewer.setLabelProvider(new ConnectionLabelProvider());
		if (hasMapEditorModel())
			treeViewer.setInput(getMapEditorModel().getConnectionCollection().toArray(new LayerUpdater[0]));

		treeViewer.refresh();
	}

	@Override
	protected void createMenu() {
		// IMenuManager mgr = getViewSite().getActionBars().getMenuManager();
		// mgr.add(selectAllAction);
	}

	@Override
	protected void createToolbar() {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(addConnectionAction);
		mgr.add(removeConnectionAction);
	}

	@Override
	protected void createActions() {
		removeConnectionAction = new Action("Disconnect") {
			public void run() {
				TreeSelection selection = ((TreeSelection) treeViewer.getSelection());
				if (hasMapEditor()) {
					if (selection.getFirstElement() instanceof LayerUpdater) {
						Shell shell = editor.getScreenManager().getDisplay().getActiveShell();
						LayerUpdater connection = (LayerUpdater) selection.getFirstElement();

						boolean feedback = MessageDialog.openQuestion(shell, "Disconnect Connection", "Would you really disconnect " + connection.getQuery().getLogicalQuery().getQueryText());
						if (feedback) {
							editor.removeConnection(connection);
							LOG.debug("Remove Connection");
						}
					}
				}
			}
		};

		addConnectionAction = new Action("Connect") {
			public void run() {

				if (hasMapEditor()) {
					// Shell shell =
					// editor.getScreenManager().getDisplay().getActiveShell();
					// PropertyTitleDialog dialog = new
					// PropertyTitleDialog(shell, model.getLayers(),
					// model.getConnectionCollection());
					// dialog.create();
					// dialog.open();
					// editor.addLayer(dialog.getLayerConfiguration());

					// ISession user = OdysseusRCPPlugIn.getActiveSession();
					IExecutor executor = OdysseusRCPPlugIn.getExecutor();
					IServerExecutor serverExecutor = null;

					if (executor instanceof IServerExecutor) {
						serverExecutor = (IServerExecutor) executor;
						Collection<IPhysicalQuery> queries = serverExecutor.getExecutionPlan().getQueries();

						// for (IPhysicalQuery iPhysicalQuery : queries) {
						// List<IPhysicalOperator> ops =
						// serverExecutor.getPhysicalRoots(iPhysicalQuery.getID());
						// ops.isEmpty();
						// }

						IStructuredContentProvider contentprovider = new IStructuredContentProvider() {
							Collection<IPhysicalQuery> operator;

							@Override
							public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
								this.operator = (Collection<IPhysicalQuery>) newInput;

							}

							@Override
							public void dispose() {
								// TODO Auto-generated method stub

							}

							@Override
							public Object[] getElements(Object inputElement) {
								// TODO Auto-generated method stub
								return this.operator.toArray();
							}
						};

						ILabelProvider labelprovider = new ILabelProvider() {

							@Override
							public void removeListener(ILabelProviderListener listener) {
								// TODO Auto-generated method stub

							}

							@Override
							public boolean isLabelProperty(Object element, String property) {
								// TODO Auto-generated method stub
								return false;
							}

							@Override
							public void dispose() {
								// TODO Auto-generated method stub

							}

							@Override
							public void addListener(ILabelProviderListener listener) {
								// TODO Auto-generated method stub

							}

							@Override
							public String getText(Object element) {
								// TODO Auto-generated method stub
								return ((IPhysicalQuery) element).getLogicalQuery().getQueryText();
							}

							@Override
							public Image getImage(Object element) {
								// TODO Auto-generated method stub
								return null;
							}
						};
						ListSelectionDialog dlg = new ListSelectionDialog(Display.getCurrent().getActiveShell(), queries, contentprovider, labelprovider, "Select the resources to save:");

						// dlg..setInitialSelections(dirtyEditors);
						dlg.setTitle("Select Map Stream Connections");
						dlg.open();
						
						if(dlg.getReturnCode() == MessageDialog.OK){
							Object[] sel = dlg.getResult();
							for (Object object : sel) {
								IPhysicalQuery op = (IPhysicalQuery) object;
								editor.addConnection(op);
	
							}
						}
					}
				}
			}
		};

	}
}
