package de.uniol.inf.is.odysseus.broker.console.views;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.broker.console.ViewController;

public class OutputTabView{	
	private TableViewer viewer;
	private OutputView parentView;
	private int port;
	private String[] columns;
	
	public OutputTabView(OutputView parent, int port, String[] columns){
		this.parentView = parent;
		this.port = port;
		this.columns = columns;
	}
	
	public Composite createPartControl(Composite tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		viewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		createColumns(viewer);
		viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.setContentProvider(ViewController.getInstance()
				.getContentProvider(port));
		viewer.setLabelProvider(new ViewLabelProvider());	
		viewer.setInput(parentView.getViewSite());					
		
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(),
				"de.uniol.inf.is.odysseus.broker.console.viewer");
		
		return composite;
										
	}
	
	
	private void createColumns(TableViewer viewer) {				
		for (int i = 0; i < columns.length; i++) {
			TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
			column.getColumn().setText(columns[i]);
			column.getColumn().setWidth(100);
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(true);
		}
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	
	
	public void refreshViewer() {
		this.viewer.getTable().clearAll();
		this.viewer.refresh();		
	}

	public void clearTable() {
		this.viewer.getTable().clearAll();
		ViewController.getInstance()
		.getContentProvider(port).clear();
		
	}
	
	

}
