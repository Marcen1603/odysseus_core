package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class MapQueryFilesView extends AbstractStreamMapEditorViewPart {

//	private static final Logger LOG = LoggerFactory.getLogger(MapQueryFilesView.class);

	private TreeViewer treeViewer;

	@Override
	public void setFocus() {

	}

	@Override
	public void updatePartControl(Composite parent) {
		// LOG.debug("Update Query Files");
		treeViewer = new TreeViewer(parent);
		treeViewer.setContentProvider(new ConnectionTreeContentProvider());
		treeViewer.setLabelProvider(new ConnectionLabelProvider());
		if (hasMapEditorModel()) {
			treeViewer.setInput(getMapEditorModel().getFiles().toArray());
			model.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent arg0) {
					if (hasMapEditorModel()) {
						treeViewer.setInput(getMapEditorModel().getFiles().toArray());
						treeViewer.refresh();
					}

				}
			});
		}

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
		mgr.add(new Action("Add") {
			public void run() {

				if (hasMapEditorModel()) {
					ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent.getShell(), new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
					dialog.setTitle("Query File Selection");
					dialog.setMessage("Select the files from the List:");
					dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
					dialog.open();
					if (dialog.getReturnCode() == MessageDialog.OK) {
						Object[] sel = dialog.getResult();
						for (Object object : sel) {
							if (object instanceof IFile) {
								IFile newFile = (IFile) object;
								if (newFile.getFileExtension().equalsIgnoreCase("qry")) {
									if(hasMapEditor())
										getMapEditor().addFile(newFile);
									
									
								} else {
									MessageDialog.openError(parent.getShell(), "File Type Error", "Only the file extension .qry is allowed.");
								}
							} else {
								MessageDialog.openError(parent.getShell(), "File Type Error", "Only the file extension .qry is allowed.");
							}
						}
					}

				}

			}
		});
		mgr.add(new Action("Remove") {
			public void run() {
				if (hasMapEditor()) {
					IFile file = (IFile) ((TreeSelection) treeViewer.getSelection()).getFirstElement();
					editor.removeFile(file);
					treeViewer.refresh();
				}
			}
		});
	}

	@Override
	protected void createActions() {

	}
}
