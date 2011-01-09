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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class SourceTable extends Composite {
	
	private SourceTableModel model;
	//private ArrayList<AttributeDialog> dialogs;
	
	TableViewer tv;
//	ToolBar tb_attList;

	
	public SourceTable(Composite parent, int style) {
		super(parent, style);
		model = SourceTableModel.getInstance();
		
		this.setLayout(new GridLayout(1,false));
		
		tv = new TableViewer(this);
		tv.setContentProvider(new ISourceContentProvider());
		tv.setLabelProvider(new ISourceLabelProvider());
		
		Table sourceTable = tv.getTable();
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

		//TODO: hier evtl noch buttons hinsetzen, dann aber etwas commandbasiertes verwenden	
		update(model.getAllElements());
	}
	
	public void addElement(ISource source){
		model.addSource(source);
		update(model.getAllElements());
	}
	
	public void delElement(int index){
		if(model.getSource(index) != null){
			model.deleteSource(index);
		}
		update(model.getAllElements());
	}
	
	private void update(ArrayList<ISource> sourceList){
		tv.setInput(sourceList);
	}
		
	
	
	static class SourceTableModel {
		/*
		 * Singleton SourceTableModel, represents the sourceslist
		 */
		private static SourceTableModel instance = new SourceTableModel();
		private ArrayList<ISource> sourcesList;
		
		private SourceTableModel(){
			sourcesList = new ArrayList<ISource>();
			MetMast mm1 = new MetMast("MetMast1","MeteoSim","localhost",50001,new Attribute[]{new Attribute("att1",Attribute.AttributeType.AIRPRESSURE),new Attribute("att2",Attribute.AttributeType.POWER)});
			WindTurbine wt1 = new WindTurbine("WT1","flapSim","localhost",50002,new Attribute[]{new Attribute("att1",Attribute.AttributeType.AIRPRESSURE),new Attribute("att2",Attribute.AttributeType.POWER)},55.0,true);
			sourcesList.add(mm1);
			sourcesList.add(wt1);
		}
		
		public static SourceTableModel getInstance(){
			return instance;
		}
		
		public void addSource(ISource source){
			sourcesList.add(source);
		}
		
		public ISource getSource(int index){
			return sourcesList.get(index);
		}
		
		public ArrayList<ISource> getAllElements(){
			return(sourcesList);
		}
		
		public void deleteSource(int index){
			if(sourcesList.get(index)!=null){
				sourcesList.remove(index);
			}
		}
		
		public int getElemCount(){
			return sourcesList.size();
		}
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
