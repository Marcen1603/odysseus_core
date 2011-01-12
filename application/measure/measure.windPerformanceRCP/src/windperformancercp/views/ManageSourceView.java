package windperformancercp.views;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import windperformancercp.event.IEvent;
import windperformancercp.event.IEventListener;
import windperformancercp.event.UpdateEvent;
import windperformancercp.event.UpdateEventType;
import windperformancercp.model.sources.ISource;

public class ManageSourceView extends ViewPart {
	public static final String ID = "measure.windPerformanceRCP.sourceManagerView";

	ManageSourcePresenter presenter = new ManageSourcePresenter(this);
	
	//SourceTable sourceTabViewer;
	TableViewer sourceViewer;

	
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		//TODO: das hier besser in einen konstruktor?
		
		//surrounding sash form
		SashForm sashForm = new SashForm(parent, SWT.BORDER | SWT.SMOOTH);
		sashForm.setSashWidth(5);

		
		//sources list composite with buttons
		
		Composite leftComposite = new Composite(sashForm, SWT.NONE);
		leftComposite.setLayout(new GridLayout(1,false));
		
		/*ToolBar slTB = new ToolBar(leftComposite,SWT.BORDER);
		ToolItem itm_add = new ToolItem(slTB,SWT.PUSH);
		*/
		//sourceTabViewer = new SourceTable(leftComposite,SWT.FILL);
		//sourceTabViewer.setLayoutData(new GridData(GridData.FILL_BOTH));
		sourceViewer = new TableViewer(leftComposite);
		sourceViewer.setContentProvider(new ISourceContentProvider());
		sourceViewer.setLabelProvider(new ISourceLabelProvider());
		
		Table sourceTable = sourceViewer.getTable();
		{
			sourceTable.setLayoutData(new GridData(GridData.FILL_BOTH));
			
		
			String[] titles ={"Name","Type","Host","Port"};
			for (int i=0;i<titles.length;i++){
				TableColumn col = new TableColumn(sourceTable,SWT.LEFT);
				col.setText(titles[i]);
				//col.pack();
				col.setWidth(100);
			}
			
			sourceTable.setHeaderVisible(true);
			sourceTable.setLinesVisible(true);
		}
		//right composite for additional information
		Composite rightDetailedComposite = new Composite(sashForm, SWT.NONE);
		
		presenter.subscribeToAll(updateListener);
		System.out.println("subscribed to invocation");
	}

	IEventListener updateListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> idevent){
			if(idevent.getEventType().equals(UpdateEventType.GeneralUpdate)){ //doppelt gemoppelt? ich registriere ja nur fuer newattitem
				UpdateEvent updateInvoker = (UpdateEvent) idevent;
				//ArrayList<?> list = new ArrayList<?>(updateInvoker.getValue());
			System.out.println(this.toString()+"got general update invocation");
				update(updateInvoker.getValue());
			}
			
		}
	};

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
	}
	
	public TableItem[] getSources(){
		return this.sourceViewer.getTable().getItems();
	}
	
	public void update(ArrayList<?> newList){
		sourceViewer.setInput(newList);
	}
	
	
	class ISourceLabelProvider implements ITableLabelProvider{

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		//TODO
		@Override
		public String getColumnText(Object element, int columnIndex) {
			ISource source = (ISource) element;
			switch(columnIndex){
			case 0: return source.getName();
			case 1:	if(source.isWindTurbine()) return "WindTurbine";
			if(source.isMetMast()) return "MetMast";
			else return "Other";
			case 2: return source.getHost();
			case 3:	return Integer.toString(source.getPort());
			
			}
			return null;
		}
		
	}

	class ISourceContentProvider implements IStructuredContentProvider{

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object[] getElements(Object inputElement) {
			return ((ArrayList<ISource>)inputElement).toArray();
		}
		
	}
}