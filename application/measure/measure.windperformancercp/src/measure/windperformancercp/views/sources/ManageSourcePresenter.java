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

import measure.windperformancercp.controller.IController;
import measure.windperformancercp.controller.SourceController;
import measure.windperformancercp.event.EventHandler;
import measure.windperformancercp.event.IEvent;
import measure.windperformancercp.event.IEventListener;
import measure.windperformancercp.event.InputDialogEvent;
import measure.windperformancercp.event.InputDialogEventType;
import measure.windperformancercp.event.ModelEventType;
import measure.windperformancercp.event.UpdateEvent;
import measure.windperformancercp.event.UpdateEventType;
import measure.windperformancercp.model.IDialogResult;
import measure.windperformancercp.model.sources.SourceModel;
import measure.windperformancercp.views.IPresenter;

import org.eclipse.ui.IActionBars;


public class ManageSourcePresenter extends EventHandler implements IPresenter{
	
	ManageSourceView view;
	static IController _cont;
	static SourceModel model;
	
	public ManageSourcePresenter(ManageSourceView view){	
		//System.out.println(this.toString()+": manage source presenter says hi!");
		this.view = view;
		_cont = SourceController.getInstance(this);
		model = SourceModel.getInstance();
		
		model.subscribeToAll(modelListener);
		fire(new InputDialogEvent(this,InputDialogEventType.RegisterDialog,null));
	}
	
	public void updateView(){
		try{
			//fire(new UpdateEvent(this,UpdateEventType.GeneralUpdate,_cont.getContent()));
			fire(new UpdateEvent(this,UpdateEventType.GeneralUpdate,model.getSourcesListB()));
		}
		catch(Exception e){
			System.out.println(this.toString()+": "+e);
		}
	}
	
	
	public IEventListener modelListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> event){
			
			if(event.getEventType().equals(ModelEventType.NewItem)){ 
				updateView();
				setStatusLine("Added source successfully. ");
			}
			
			if(event.getEventType().equals(ModelEventType.DeletedItem)){ 
				updateView();
				setStatusLine("Removed source successfully. ");
			}
			
			if(event.getEventType().equals(ModelEventType.ModifyItem)){ 
				updateView();
				setStatusLine("Changed source successfully. ");
			}
		}
	};

	private void setStatusLine(String message) {
		// Get the status line and set the text
		IActionBars bars = view.getViewSite().getActionBars();
		bars.getStatusLineManager().setMessage(message);
	}
	
	@Override
	public void notifyUser(String message){
		setStatusLine(message);
	}
	
	@Override
	public void subscribeToAll(IEventListener listener) {
		super.subscribeToAll(listener);
		if(listener.equals(view.updateListener)){
			updateView();
		}
	}
	

	@Override
	public void feedDialog(IDialogResult input) {
		//updateView();
	}
	
}