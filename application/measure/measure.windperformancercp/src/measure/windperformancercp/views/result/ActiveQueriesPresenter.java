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

import measure.windperformancercp.controller.Connector;
import measure.windperformancercp.controller.IController;
import measure.windperformancercp.controller.PMController;
import measure.windperformancercp.event.EventHandler;
import measure.windperformancercp.event.IEvent;
import measure.windperformancercp.event.IEventListener;
import measure.windperformancercp.event.InputDialogEvent;
import measure.windperformancercp.event.InputDialogEventType;
import measure.windperformancercp.event.ModelEventType;
import measure.windperformancercp.event.UpdateEvent;
import measure.windperformancercp.event.UpdateEventType;
import measure.windperformancercp.model.IDialogResult;
import measure.windperformancercp.model.query.PerformanceModel;
import measure.windperformancercp.views.IPresenter;
import measure.windperformancercp.views.performance.FunctionPlotterView;

import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

/**
 * Manage the communication between ActiveQueriesView and PerformanceController
 * @author Diana von Gallera
 *
 */
public class ActiveQueriesPresenter extends EventHandler implements IPresenter{
	
	private static ActiveQueriesPresenter instance = new ActiveQueriesPresenter(); 
	private static ActiveQueriesView view;
	IController _cont;
	private static List<FunctionPlotterView> plotterViews;
	boolean alreadyConnected = false;
	
	static PerformanceModel pmodel;
	
	private ActiveQueriesPresenter(){
		//instance = this;
		plotterViews = new ArrayList<FunctionPlotterView>();
		_cont = PMController.getInstance(this);
		pmodel = PerformanceModel.getInstance();
		pmodel.subscribeToAll(modelListener);
		fire(new InputDialogEvent(this,InputDialogEventType.RegisterDialog,null));
		//System.out.println(this.toString()+": result plot presenter says hi from no-arg-contructor!");
	}
	
	/**
	 * The constructor for the view.
	 * @param caller
	 */
	private ActiveQueriesPresenter(ActiveQueriesView caller){	
		//instance = this;
		view = caller;
		plotterViews = new ArrayList<FunctionPlotterView>();
		_cont = PMController.getInstance(this);
		pmodel = PerformanceModel.getInstance();
		pmodel.subscribeToAll(modelListener);
		fire(new InputDialogEvent(this,InputDialogEventType.RegisterDialog,null));
		//System.out.println(this.toString()+": result plot presenter says hi!");
	}
	
	/**
	 * 'Updates' the AssignPerformanceMeasView via update event
	 */
	public void updateView(){
		//fire(new UpdateEvent(this,UpdateEventType.GeneralUpdate,_cont.getContent()));
		List<String> list = new ArrayList<String>();
		list.addAll(Connector.getInstance().getQueries().keySet());
		fire(new UpdateEvent(this,UpdateEventType.GeneralUpdate,list));
	}
		
	
	//EVENT HANDLING
	@Override
	public void subscribeToAll(IEventListener listener) {
		super.subscribeToAll(listener);
		if(view != null){
			if(listener.equals(view.updateListener)){
				updateView();
			}
		}
	}
	
	/**
	 * The event listener for the performance model
	 */
	public IEventListener modelListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> event){
			
			if(event.getEventType().equals(ModelEventType.ModifyItem)){ 
				//System.out.println("received new measurement event, updating view!");
				updateView();
				setStatusLine("A query has been modified. ");
			}
			
			if(event.getEventType().equals(ModelEventType.DeletedItem)){ 
				//System.out.println("received new measurement event, updating view!");
				updateView();
				setStatusLine("A query has been deleted. ");
			}
			
		
		}
	};

	/**
	 * Sets the status line for the user.
	 * @param message What we want to tell the user.
	 */
	private void setStatusLine(String message) {
		// Get the status line and set the text
		IActionBars bars = view.getViewSite().getActionBars();
		bars.getStatusLineManager().setMessage(message);
	}
	
	@Override
	public void notifyUser(String message){
		setStatusLine(message);
	}
	
	public void connectViewToQuery(String qid){
	//System.out.println(this.toString()+" called connectViewToQuery. query id: "+qid+" plotter views size: "+plotterViews.size());	
		if(plotterViews.size() > 0) {
			plotterViews.get(0).connectToQuery(qid);	//hm, das geht besser. Falls es mal mehr als einen view geben sollte, sollte der betreffende damit verbunden werden (hash map (string, function plotter)
			alreadyConnected = true;
		}
	}


	@Override
	public void feedDialog(IDialogResult input) {
		// TODO Auto-generated method stub
	}
	
	public static ActiveQueriesPresenter getInstance(){
		return instance;
	}
	
	public static <V extends ViewPart> ActiveQueriesPresenter getInstance(V nview){
		if(nview instanceof ActiveQueriesView){
			view = (ActiveQueriesView) nview;
		}
		
		if(nview instanceof FunctionPlotterView){
		//	System.out.println(" resultplotpresenter: called getInstance for function plotter. ");
			plotterViews.add((FunctionPlotterView)nview);
		}
		//	registerPresenter(pwdPresenters, (PerformanceWizardDialogPresenter) pres);
		
		return instance;
	}
}