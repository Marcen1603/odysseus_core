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
package measure.windperformancercp.views.result;

import java.util.ArrayList;
import java.util.List;

import measure.windperformancercp.event.IEvent;
import measure.windperformancercp.event.IEventListener;
import measure.windperformancercp.event.UpdateEvent;
import measure.windperformancercp.event.UpdateEventType;
import measure.windperformancercp.views.IPresenter;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

public class ActiveQueriesView extends ViewPart {
	public static final String ID = "measure.windperformancercp.activeQueriesView";

	//ResultPlotPresenter presenter = new ResultPlotPresenter(this);
	
	ActiveQueriesPresenter presenter; 
	
	private TableViewer queryViewer;
	
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		presenter = ActiveQueriesPresenter.getInstance(this);
		
		//performance list composite with buttons
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1,false));

		queryViewer = new TableViewer(composite);
		queryViewer.setContentProvider(new IQueryContentProvider());
		queryViewer.setLabelProvider(new IQueryLabelProvider());
		
		Table sourceTable = queryViewer.getTable();
		{
			sourceTable.setLayoutData(new GridData(GridData.FILL_BOTH));
			
		
			String[] titles ={"Id"};
			for (int i=0;i<titles.length;i++){
				TableColumn col = new TableColumn(sourceTable,SWT.LEFT);
				col.setText(titles[i]);
				//col.pack();
				col.setWidth(90);
			}
		
			sourceTable.setHeaderVisible(true);
			sourceTable.setLinesVisible(true);
		}
	
		queryViewer.addSelectionChangedListener(selectionListener);
		getSite().setSelectionProvider(queryViewer);
		presenter.subscribeToAll(updateListener);
		
	//	MenuManager menuManager = new MenuManager();
	//	Menu menu = menuManager.createContextMenu(queryViewer.getTable());
		// Set the MenuManager
	//	queryViewer.getTable().setMenu(menu);
	//	getSite().registerContextMenu(menuManager, queryViewer);
	}

	ISelectionChangedListener selectionListener = new ISelectionChangedListener(){
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			int id = queryViewer.getTable().getSelectionIndex();
			if(id >= 0){
				String itemName = queryViewer.getTable().getItem(id).getText(0);
				presenter.connectViewToQuery(itemName);
			}
			//queryViewer.getTable().getSelectionIndex()
		//	queryView.setText(presenter.getQueryText(queryViewer.getTable().getSelectionIndex()));
		}
	};
	
	IEventListener updateListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> idevent){
			if(idevent.getEventType().equals(UpdateEventType.GeneralUpdate)){
				//System.out.println(this.toString()+"got general update invocation");
				UpdateEvent updateInvoker = (UpdateEvent) idevent;
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
	
	public TableItem[] getQueries(){
		return this.queryViewer.getTable().getItems();
	}
	
	
	public void update(List<?> newList){
		queryViewer.setInput(newList);
		if(!newList.isEmpty()){
			int ind = queryViewer.getTable().getSelectionIndex();
			
			if((ind != -1)&&(ind < queryViewer.getTable().getItemCount())){
				queryViewer.getTable().select(ind);
			}
			else
				queryViewer.getTable().select(0);
			//queryView.setText(presenter.getQueryText(performanceViewer.getTable().getSelectionIndex()));
			int id = queryViewer.getTable().getSelectionIndex();
			String itemName = queryViewer.getTable().getItem(id).getText(0);
			presenter.connectViewToQuery(itemName);
			
		}
	}
	
	public void setQueryView(String input){
		//queryView.setText(input);
	}
	

	public IPresenter getPresenter(){
		return presenter;
	}
	
	
	class IQueryLabelProvider implements ITableLabelProvider{

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

		//TODO: "PMName","Type","Sources","Lifetime"
		@Override
		public String getColumnText(Object element, int columnIndex) {
			//IPerformanceQuery query = (IPerformanceQuery) element;
			//IQuery query = (IQuery) element;
			switch(columnIndex){
			case 0: return element.toString();
			//case 0: return Integer.toString(query.getID());
			/*case 1:	return query.getMethod().toString();
			case 2: return Integer.toString(query.getConcernedSrcKeys().size());
			case 3:	return Double.toString(0.0);
			case 4:	return Boolean.toString(query.getConnectStat());*/ 
			}
			return null;
		}
		
	}

	class IQueryContentProvider implements IStructuredContentProvider{

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
			//return ((ArrayList<IQuery>)inputElement).toArray();
			return ((ArrayList<String>)inputElement).toArray();
		}
		
	}
}