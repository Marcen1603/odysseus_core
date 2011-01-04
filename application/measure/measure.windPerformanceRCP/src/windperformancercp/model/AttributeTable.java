package windperformancercp.model;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import windperformancercp.model.Attribute.AttributeType;
import windperformancercp.views.AttributeDialog;

public class AttributeTable extends Composite {

	private AttributeTableModel model;
	
	TableViewer tv;
	ToolBar tb_attList;

	public AttributeTable(Composite parent, int style) {
		super(parent, style);
		model = new AttributeTableModel();
		
		this.setLayout(new GridLayout(2,false));
		
		tv = new TableViewer(this);
		tv.setContentProvider(new AttributeContentProvider());
		tv.setLabelProvider(new AttributeLabelProvider());
		
		Table attributeTable = tv.getTable();
		{
			attributeTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		
			String[] titles ={"Attribute","Type"};
			for (int i=0;i<titles.length;i++){
				TableColumn col = new TableColumn(attributeTable,SWT.LEFT);
				col.setText(titles[i]);
				col.pack();
			}
			attributeTable.setHeaderVisible(true);
			attributeTable.setLinesVisible(true);
		}

		tb_attList = new ToolBar(this,SWT.BORDER|SWT.VERTICAL);
		{
			String[] ti_labels = {"Add","Up","Down","Delete"};
			for(int i =0;i<ti_labels.length;i++){
				ToolItem ti = new ToolItem(tb_attList,SWT.PUSH);
				ti.setText(ti_labels[i]);
				ti.addListener(SWT.Selection, selectionListener);
			}
			GridData gd_tb_attL = new GridData(60,SWT.DEFAULT);
			gd_tb_attL.horizontalAlignment = GridData.BEGINNING;
			tb_attList.setLayoutData(gd_tb_attL);
		}
		update(model.getAllAttributes());

	}

	private void update(ArrayList<Attribute> attList){
		tv.setInput(attList);
	}
	
	Listener selectionListener = new Listener() {
	      public void handleEvent(Event event) {
	        ToolItem item = (ToolItem)event.widget;
	       
	        if(item.getText().equals("Add")){
	        	try {
	        		//TODO: das hier geht sicher eleganter mittels eines Commands!
	        		final Shell dialogShell = new Shell(event.display.getActiveShell());
	        		AttributeDialog dialog = new AttributeDialog(dialogShell);
					//TODO: generate
	        		if(dialog.open() == Window.OK)
	        			System.out.println("New Attribute Handler says: Dialog says - ok button has been pressed!");
	        			
	        		else			
	        			System.out.println("New Attribute Handler says: Dialog says - cancel button has been pressed!");
	    	   }
	    	   catch(Exception ex){
	    		   System.out.println("Exception in pressing "+item.getText()+": "+ex+";;;"+ex.getCause()+";;;"+ex.getMessage());
	    	   }

	       }
	        System.out.println(item.getText() + " is selected");
	        if( (item.getStyle() & SWT.RADIO) != 0 || (item.getStyle() & SWT.CHECK) != 0 ) 
	        	System.out.println("Selection status: " + item.getSelection());
	      }
	    };

	
	
	class AttributeTableModel{
		private ArrayList<Attribute> attributeList;
		
		public AttributeTableModel(){			
			attributeList = new ArrayList<Attribute>();
			attributeList.add(new Attribute("test1",AttributeType.AIRPRESSURE));
			//attributeList.add(new Attribute("test2",AttributeType.WINDSPEED));
			//attributeList.add(new Attribute("test3",AttributeType.POWER));
			
		}
		
		
		public void addAttribute(Attribute newAtt){
			attributeList.add(newAtt);
		}

		public void deleteAttribute(int index){
			if(attributeList.get(index)!=null)
			attributeList.remove(index);
		}
		
		public Attribute getAttribute(int index){
			return(attributeList.get(index));
		}
		
		public ArrayList<Attribute> getAllAttributes(){
			return(attributeList);
		}
	}
	

	class AttributeLabelProvider implements ITableLabelProvider{

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

		@Override
		public String getColumnText(Object element, int columnIndex) {
			Attribute attr = (Attribute) element;
			switch(columnIndex){
			case 0: return attr.getName();
			case 1:	return attr.getAttType();
			}
			return null;
		}
		
	}

	class AttributeContentProvider implements IStructuredContentProvider{

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return ((ArrayList<Attribute>)inputElement).toArray();
		}
		
	}
}
