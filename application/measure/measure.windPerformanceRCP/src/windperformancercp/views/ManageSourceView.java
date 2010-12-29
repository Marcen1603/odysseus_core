package windperformancercp.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class ManageSourceView extends ViewPart {
	public static final String ID = "measure.windPerformanceRCP.sourceManagerView";

	
	private ListViewer listViewer;

	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	class ViewContentProvider implements IStructuredContentProvider {
		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object parent) {
			if (parent instanceof Object[]) {
				return (Object[]) parent;
			}
	        return new Object[0];
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		@Override
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		//surrounding sash form
		SashForm sashForm = new SashForm(parent, SWT.BORDER | SWT.SMOOTH);
		sashForm.setSashWidth(5);

		
		//sources list composite with buttons
		
		Composite leftComposite = new Composite(sashForm, SWT.NONE);
		leftComposite.setLayout(new GridLayout());
		
		GridData slGridData = new GridData(GridData.FILL_BOTH);
		listViewer = new ListViewer(leftComposite, SWT.BORDER | SWT.V_SCROLL);
		
		List sourcesList = listViewer.getList();
		sourcesList.setLayoutData(slGridData);
		sourcesList.add("TestSource");
		
		//right composite for additional information
		Composite rightDetailedComposite = new Composite(sashForm, SWT.NONE);
		
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		listViewer.getControl().setFocus();
	}
}