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
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
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
		ToolBar sourceToolBar = new ToolBar(leftComposite, SWT.BORDER | SWT.FLAT | SWT.RIGHT);
		sourceToolBar.setLayoutData(new GridData());
		//MenuManager tbManager = new MenuManager();
		
		/*
		ToolItem tltmNew = new ToolItem(sourceToolBar, SWT.NONE);
		tltmNew.setToolTipText("create a new source");
		tltmNew.setText("New");
		*/
		
		/*CommandContributionItemParameter parms = 
				new CommandContributionItemParameter(getSite(),
						"",
						"measure.windPerformanceRCP.showNewSourceDialog",
						CommandContributionItem.STYLE_PUSH);
		parms.label = "New";
		CommandContributionItem tltmNew = new CommandContributionItem(parms);
		tltmNew.fill(sourceToolBar);
		*/
				
		ToolItem tltmCopy = new ToolItem(sourceToolBar, SWT.NONE);
		tltmCopy.setToolTipText("copy the selected source");
		tltmCopy.setText("Copy");
		
		ToolItem tltmDelete = new ToolItem(sourceToolBar, SWT.NONE);
		tltmDelete.setToolTipText("delete the selected source");
		tltmDelete.setText("Delete");
		
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
		//viewer.getControl().setFocus();
		listViewer.getControl().setFocus();
	}
}