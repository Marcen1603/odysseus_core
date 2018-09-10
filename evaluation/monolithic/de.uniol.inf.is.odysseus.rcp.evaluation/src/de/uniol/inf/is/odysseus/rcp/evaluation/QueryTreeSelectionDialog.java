package de.uniol.inf.is.odysseus.rcp.evaluation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class QueryTreeSelectionDialog extends ElementTreeSelectionDialog {

	private IResource resource;

	public QueryTreeSelectionDialog(Shell parent, IResource resource) {
		super(parent, new WorkbenchLabelProvider(), new WorkbenchContentProvider());
		this.resource = resource;
		init();		
	}
	
	
	private void init(){		
		setTitle("Choose Query");
		setMessage("Choose a query file");
		setInput(resource.getProject());
		setAllowMultiple(false);
		addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof IFile) {
					IFile res = (IFile) element;
					if (validFileResouce(res)) {
						return true;
					}
					return false;
				}
				return true;
			}

		});
		setValidator(new ISelectionStatusValidator() {

			@Override
			public IStatus validate(Object[] selection) {
				if (selection.length != 1) {
					return new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, "You have to choose a single query");
				}
				Object sel = selection[0];
				if (sel instanceof IFile) {
					IFile file = (IFile) sel;
					if (validFileResouce(file)) {
						return Status.OK_STATUS;
					}
				}
				return new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, "You can only choose a query file");
			}
		});
		

		
	}
	
	private boolean validFileResouce(IFile res) {
		return res.getFileExtension().equalsIgnoreCase("qry");
	}

}
