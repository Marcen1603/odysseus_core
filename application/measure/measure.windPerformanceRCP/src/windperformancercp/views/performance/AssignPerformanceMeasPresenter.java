package windperformancercp.views.performance;

import windperformancercp.controller.IController;
import windperformancercp.controller.PMController;
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

public class AssignPerformanceMeasPresenter extends EventHandler implements IPresenter{
	
	AssignPerformanceMeasView view;
	IController _cont;
	//TODO
	SourceModel smodel;
	
	public AssignPerformanceMeasPresenter(AssignPerformanceMeasView view){	
		this.view = view;
		_cont = PMController.getInstance(this);
		//model = SourceModel.getInstance();
		//System.out.println(this.toString()+": manage source presenter says hi!");
		//model.subscribeToAll(modelListener);
		fire(new InputDialogEvent(this,InputDialogEventType.RegisterDialog,null));
		
	}
	
	public void updateView(){
		fire(new UpdateEvent(this,UpdateEventType.GeneralUpdate,_cont.getContent()));
	}
	
	
	public IEventListener modelListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> event){
			
			/*if(event.getEventType().equals(SourceModelEventType.NewItem)){ 
				System.out.println("received new source event, updating view!");
				updateView();
			}
			
			if(event.getEventType().equals(SourceModelEventType.DeletedItem)){ 
				System.out.println("received delete source event, updating view!");
				updateView();
			}
			*/
		}
	};



	@Override
	public void feedDialog(IDialogResult input) {
		// TODO Auto-generated method stub
		
	}
}