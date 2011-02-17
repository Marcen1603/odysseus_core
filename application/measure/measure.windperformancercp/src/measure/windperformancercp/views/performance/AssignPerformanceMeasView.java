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
package measure.windperformancercp.views.performance;

import java.util.ArrayList;

import measure.windperformancercp.event.IEvent;
import measure.windperformancercp.event.IEventListener;
import measure.windperformancercp.event.UpdateEvent;
import measure.windperformancercp.event.UpdateEventType;
import measure.windperformancercp.model.query.IPerformanceQuery;
import measure.windperformancercp.views.IPresenter;

import org.eclipse.jface.action.MenuManager;
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class AssignPerformanceMeasView extends ViewPart {
	public static final String ID = "measure.windperformancercp.assignPMView";

	private AssignPerformanceMeasPresenter presenter = new AssignPerformanceMeasPresenter(this);
	
	private TableViewer performanceViewer;
	Text queryView;

	
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		//surrounding sash form
		SashForm sashForm = new SashForm(parent, SWT.BORDER | SWT.SMOOTH);
		sashForm.setSashWidth(5);

		
		//performance list composite with buttons
		Composite leftComposite = new Composite(sashForm, SWT.NONE);
		leftComposite.setLayout(new GridLayout(1,false));
		
		/*ToolBar slTB = new ToolBar(leftComposite,SWT.BORDER);
		ToolItem itm_add = new ToolItem(slTB,SWT.PUSH);
		*/

		performanceViewer = new TableViewer(leftComposite);
		performanceViewer.setContentProvider(new ISourceContentProvider());
		performanceViewer.setLabelProvider(new ISourceLabelProvider());
		
		Table sourceTable = performanceViewer.getTable();
		{
			sourceTable.setLayoutData(new GridData(GridData.FILL_BOTH));
			
		
			String[] titles ={"Name","Type","Sources","Lifetime","Connected"};
			for (int i=0;i<titles.length;i++){
				TableColumn col = new TableColumn(sourceTable,SWT.LEFT);
				col.setText(titles[i]);
				//col.pack();
				col.setWidth(90);
			}
		
			sourceTable.setHeaderVisible(true);
			sourceTable.setLinesVisible(true);
		}
		//right composite for additional information
		Composite rightDetailedComposite = new Composite(sashForm, SWT.NONE);
		rightDetailedComposite.setLayout(new FillLayout());
		queryView = new Text(rightDetailedComposite, SWT.READ_ONLY|SWT.MULTI|SWT.WRAP|SWT.V_SCROLL);
		
		performanceViewer.addSelectionChangedListener(selectionListener);
		getSite().setSelectionProvider(performanceViewer);
		presenter.subscribeToAll(updateListener);
		
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(performanceViewer.getTable());
		// Set the MenuManager
		performanceViewer.getTable().setMenu(menu);
		getSite().registerContextMenu(menuManager, performanceViewer);
	}

	ISelectionChangedListener selectionListener = new ISelectionChangedListener(){
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			queryView.setText(presenter.getQueryText(performanceViewer.getTable().getSelectionIndex()));
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
	
	public TableItem[] getSources(){
		return this.performanceViewer.getTable().getItems();
	}
	
	
	public void update(ArrayList<?> newList){
		performanceViewer.setInput(newList);
		if(!newList.isEmpty()){
			int ind = performanceViewer.getTable().getSelectionIndex();
			
			if((ind != -1)&&(ind < performanceViewer.getTable().getItemCount())){
				performanceViewer.getTable().select(ind);
			}
			else
				performanceViewer.getTable().select(0);
			//queryView.setText(presenter.getQueryText(performanceViewer.getTable().getSelectionIndex()));
		}
	}
	
	public void setQueryView(String input){
		queryView.setText(input);
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

		//TODO: "Name","Type","Sources","Lifetime","Connected"
		@Override
		public String getColumnText(Object element, int columnIndex) {
			IPerformanceQuery query = (IPerformanceQuery) element;
			switch(columnIndex){
			case 0: return query.getIdentifier();
			case 1:	return query.getMethod().toString();
			case 2: return Integer.toString(query.getConcernedSrc().size());
			case 3:	return Double.toString(0.0);
			case 4:	return Boolean.toString(query.getConnectStat()); 
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
			return ((ArrayList<IPerformanceQuery>)inputElement).toArray();
		}
		
	}
}