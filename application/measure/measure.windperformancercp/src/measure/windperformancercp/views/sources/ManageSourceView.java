/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package measure.windperformancercp.views.sources;

import java.util.ArrayList;
import java.util.List;

import measure.windperformancercp.event.IEvent;
import measure.windperformancercp.event.IEventListener;
import measure.windperformancercp.event.UpdateEvent;
import measure.windperformancercp.event.UpdateEventType;
import measure.windperformancercp.model.sources.ISource;
import measure.windperformancercp.views.IPresenter;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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


public class ManageSourceView extends ViewPart {
	public static final String ID = "measure.windperformancercp.sourceManagerView";

	private ManageSourcePresenter presenter = new ManageSourcePresenter(this);
	
	private TableViewer sourceViewer;

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
		leftComposite.setLayout(new GridLayout(1,false));
		
		sourceViewer = new TableViewer(leftComposite);
		sourceViewer.setContentProvider(new ISourceContentProvider());
		sourceViewer.setLabelProvider(new ISourceLabelProvider());
		
		Table sourceTable = sourceViewer.getTable();
		{
			sourceTable.setLayoutData(new GridData(GridData.FILL_BOTH));
			
		
			String[] titles ={"Name","Type","Host","Port","Connected"};
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
		sourceViewer.addSelectionChangedListener(selectionListener);
		
		getSite().setSelectionProvider(sourceViewer);
		presenter.subscribeToAll(updateListener);
		//System.out.println(this.toString()+": subscribed to invocation");
	}

	ISelectionChangedListener selectionListener = new ISelectionChangedListener(){
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			//queryView.setText(presenter.getQueryText(sourceViewer.getTable().getSelectionIndex()));
		}
	};
	
	IEventListener updateListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> idevent){
			if(idevent.getEventType().equals(UpdateEventType.GeneralUpdate)){ 
				UpdateEvent updateInvoker = (UpdateEvent) idevent;
				
		//	System.out.println(this.toString()+"got general update invocation: "+updateInvoker.getValue().toString());
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
	
	public void update(List<?> newList){
		sourceViewer.setInput(newList);
	}
	

	public IPresenter getPresenter(){
		return presenter;
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
			case 4: return Boolean.toString(source.getConnectState());
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