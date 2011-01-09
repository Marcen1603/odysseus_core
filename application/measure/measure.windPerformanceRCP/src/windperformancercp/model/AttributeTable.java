package windperformancercp.model;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
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

import windperformancercp.event.IEvent;
import windperformancercp.event.IEventListener;
import windperformancercp.event.InputDialogEvent;
import windperformancercp.event.InputDialogEventType;
import windperformancercp.views.AbstractUIDialog;
import windperformancercp.views.AttributeDialog;

public class AttributeTable extends Composite {

	private AttributeTableModel model;
	//private ArrayList<AttributeDialog> dialogs;
	
	TableViewer tv;
	ToolBar tb_attList;

	public AttributeTable(Composite parent, int style) {
		super(parent, style);
		model = new AttributeTableModel();
		//dialogs = new ArrayList<AttributeDialog>();
		
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
		update(model.getAllElements());

	}
	
	public String extractElements(){
		String result = "{";
		for(Attribute a: model.getAllElements()){
			result = result+a.toString()+";";
		}
		result = result+"}";
		return result;
	}

	private void update(ArrayList<Attribute> attList){
		tv.setInput(attList);
	}
	
	public void resetView(){
		model.getAllElements().clear();
	}
	
	Listener selectionListener = new Listener() {
	      public void handleEvent(Event event) {
	        ToolItem item = (ToolItem)event.widget;
	       
	        if(item.getText().equals("Add")){
	        	try {
	        		//TODO: das hier geht sicher eleganter mittels eines Commands!
	        		final Shell dialogShell = new Shell(event.display.getActiveShell());
	        		AbstractUIDialog dialog = new AttributeDialog(dialogShell, Attribute.AttributeType.values());
	        		IEventListener attListener = new IEventListener(){
	        			public void eventOccured(IEvent<?, ?> idevent){
	        				if(idevent.getEventType().equals(InputDialogEventType.NewAttributeItem)){ //doppelt gemoppelt? ich registriere ja nur fuer newattitem
	        					InputDialogEvent newAttevent = (InputDialogEvent) idevent;
	        					Attribute att = new Attribute((String)newAttevent.getValue()[0],newAttevent.getValue()[1]);
	        					model.addAttribute(att);
	        					update(model.getAllElements());
	        				}
	        				
	        			}
	        		};
	        		dialog.subscribe(attListener, InputDialogEventType.NewAttributeItem);
	        		dialog.open();
	        		
	    	   }
	    	   catch(Exception ex){
	    		   System.out.println("New Attribute Listener - Exception in pressing "+item.getText()+": "+ex+";;;"+ex.getCause()+";;;"+ex.getMessage());
	    	   }
	        }
	        
	        
	        if(item.getText().equals("Up")){
	        	int actualIndex = tv.getTable().getSelectionIndex();
	        	if(actualIndex > 0){ //if it's 0, it is the topmost element
	        		model.swapEntries(actualIndex, actualIndex-1);
	        		update(model.getAllElements());
	        	}
	        }
	        
	        if(item.getText().equals("Down")){
	        	int actualIndex = tv.getTable().getSelectionIndex();
	        	if(actualIndex >= 0 && actualIndex < model.getElemCount()-1){ //if it's 0, it is the bottommost element
	        		model.swapEntries(actualIndex, actualIndex+1);
	        		update(model.getAllElements());
	        	}
	        }
	       
	        if(item.getText().equals("Delete")){
	        	int actualIndex = tv.getTable().getSelectionIndex();
	        	if(actualIndex>=0){
	        		model.deleteAttribute(actualIndex);
	        		update(model.getAllElements());
	        	}
	        }
	        
	      }
	    };

	
	
	class AttributeTableModel{
		private ArrayList<Attribute> attributeList;
		
		public AttributeTableModel(){			
			attributeList = new ArrayList<Attribute>();
			//attributeList.add(new Attribute("test1",AttributeType.AIRPRESSURE));
		}
		
		public AttributeTableModel(ArrayList<Attribute> attributes){			
			attributeList = new ArrayList<Attribute>(attributes);
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
		
		public ArrayList<Attribute> getAllElements(){
			return(attributeList);
		}
		
		public void swapEntries(int ind1, int ind2){
			Attribute tmp = attributeList.get(ind1);
			attributeList.set(ind1, attributeList.get(ind2));
			attributeList.set(ind2, tmp);
		}
		
		public int getElemCount(){
			return attributeList.size();
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
			case 1:	return attr.getAttType().toString();
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

		@SuppressWarnings("unchecked")
		@Override
		public Object[] getElements(Object inputElement) {
			return ((ArrayList<Attribute>)inputElement).toArray();
		}
		
	}
}
