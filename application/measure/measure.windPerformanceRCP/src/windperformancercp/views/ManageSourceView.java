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

import windperformancercp.model.SourceTable;

public class ManageSourceView extends ViewPart {
	public static final String ID = "measure.windPerformanceRCP.sourceManagerView";

	ManageSrcPresenter presenter;
	
	SourceTable sourceTabViewer;
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		//TODO: das hier besser in einen konstruktor?
		presenter = new ManageSrcPresenter(this); 
		
		//surrounding sash form
		SashForm sashForm = new SashForm(parent, SWT.BORDER | SWT.SMOOTH);
		sashForm.setSashWidth(5);

		
		//sources list composite with buttons
		
		Composite leftComposite = new Composite(sashForm, SWT.NONE);
		leftComposite.setLayout(new GridLayout(1,false));
		
		/*ToolBar slTB = new ToolBar(leftComposite,SWT.BORDER);
		ToolItem itm_add = new ToolItem(slTB,SWT.PUSH);
		*/
		sourceTabViewer = new SourceTable(leftComposite,SWT.FILL);
		sourceTabViewer.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		
		//right composite for additional information
		Composite rightDetailedComposite = new Composite(sashForm, SWT.NONE);
		
	}


	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
	}
	
	
	public class ManageSrcPresenter{
		ManageSourceView view;
		
		public ManageSrcPresenter(ManageSourceView caller){
			this.view = caller;
		}
	}
}