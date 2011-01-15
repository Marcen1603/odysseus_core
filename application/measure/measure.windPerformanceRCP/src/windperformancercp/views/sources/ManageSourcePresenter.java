package windperformancercp.views.sources;

import org.eclipse.ui.IActionBars;

import windperformancercp.controller.IController;
import windperformancercp.controller.SourceController;
import windperformancercp.event.EventHandler;
import windperformancercp.event.IEvent;
import windperformancercp.event.IEventListener;
import windperformancercp.event.InputDialogEvent;
import windperformancercp.event.InputDialogEventType;
import windperformancercp.event.SourceModelEventType;
import windperformancercp.event.UpdateEvent;
import windperformancercp.event.UpdateEventType;
import windperformancercp.model.sources.IDialogResult;
import windperformancercp.model.sources.SourceModel;
import windperformancercp.views.IPresenter;

public class ManageSourcePresenter extends EventHandler implements IPresenter{
	
	ManageSourceView view;
	IController _cont;
	SourceModel model;
	
	public ManageSourcePresenter(ManageSourceView view){	
		this.view = view;
		_cont = SourceController.getInstance(this);
		model = SourceModel.getInstance();
		//System.out.println(this.toString()+": manage source presenter says hi!");
		model.subscribeToAll(modelListener);
		fire(new InputDialogEvent(this,InputDialogEventType.RegisterDialog,null));
		
	}
	
	public void updateView(){
		fire(new UpdateEvent(this,UpdateEventType.GeneralUpdate,_cont.getContent()));
	}
	
	
	public IEventListener modelListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> event){
			
			if(event.getEventType().equals(SourceModelEventType.NewItem)){ 
				updateView();
				setStatusLine("Added source successfully. ");
			}
			
			if(event.getEventType().equals(SourceModelEventType.DeletedItem)){ 
				updateView();
				setStatusLine("Removed source successfully. ");
			}
		}
	};

	private void setStatusLine(String message) {
		// Get the status line and set the text
		IActionBars bars = view.getViewSite().getActionBars();
		bars.getStatusLineManager().setMessage(message);
	}

	@Override
	public void feedDialog(IDialogResult input) {
		// TODO Auto-generated method stub
		
	}
}