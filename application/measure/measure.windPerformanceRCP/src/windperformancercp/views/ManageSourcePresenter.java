package windperformancercp.views;

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
import windperformancercp.model.sources.SourceModel;

public class ManageSourcePresenter extends EventHandler implements IPresenter{
	
	ManageSourceView view;
	IController _cont;
	SourceModel model;
	
	//public ManageSourcePresenter(IControler caller, ManageSourceView view){
	public ManageSourcePresenter(ManageSourceView view){
		//this._cont = caller;
		this.view = view;
		//_cont = SourceController.getInstance();
		_cont = SourceController.getInstance(this);
		model = SourceModel.getInstance();
		System.out.println("manage source presenter says hi!");
		model.subscribeToAll(modelListener);
		fire(new InputDialogEvent(this,InputDialogEventType.RegisterDialog,null));
		
	}
	
	public void updateView(){
		fire(new UpdateEvent(this,UpdateEventType.GeneralUpdate,_cont.getContent()));
		//view.update(newList);
	}
	
	public IEventListener modelListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> event){
			if(event.getEventType().equals(SourceModelEventType.NewItem)){ //doppelt gemoppelt? ich registriere ja nur fuer newattitem
				System.out.println("received new source event, updating view!");
				updateView();
			}
		}
	};

}
