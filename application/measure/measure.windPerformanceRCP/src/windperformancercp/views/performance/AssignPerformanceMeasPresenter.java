package windperformancercp.views.performance;

import org.eclipse.ui.IActionBars;

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
import windperformancercp.model.query.PerformanceModel;
import windperformancercp.model.sources.IDialogResult;
import windperformancercp.model.sources.SourceModel;
import windperformancercp.views.IPresenter;

public class AssignPerformanceMeasPresenter extends EventHandler implements IPresenter{
	
	AssignPerformanceMeasView view;
	IController _cont;
	//TODO
	SourceModel smodel;
	PerformanceModel pmodel;
	
	public AssignPerformanceMeasPresenter(AssignPerformanceMeasView view){	
		//System.out.println(this.toString()+": manage source presenter says hi!");
		this.view = view;
		_cont = PMController.getInstance(this);
		//smodel = SourceModel.getInstance();
		//smodel.subscribeToAll(modelListener);
		pmodel = PerformanceModel.getInstance();
		pmodel.subscribeToAll(modelListener);
		fire(new InputDialogEvent(this,InputDialogEventType.RegisterDialog,null));
		
	}
	
	public void updateView(){
		fire(new UpdateEvent(this,UpdateEventType.GeneralUpdate,_cont.getContent()));
	}
	
	public String getQueryText(int i){
	int j = pmodel.getElemCount();
		if(j!=0){
			if((i!=-1)&&(j>i))
				return pmodel.getIthElement(i).getQueryText();
			else
				return pmodel.getIthElement(0).getQueryText();
		}
		return "";
	}
	
	@Override
	public void subscribeToAll(IEventListener listener) {
		super.subscribeToAll(listener);
		if(listener.equals(view.updateListener)){
			updateView();
		}
	}
	
	public IEventListener modelListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> event){
			
			if(event.getEventType().equals(SourceModelEventType.NewItem)){ 
				//System.out.println("received new measurement event, updating view!");
				updateView();
				setStatusLine("Added measurement successfully. ");
			}
			
			if(event.getEventType().equals(SourceModelEventType.DeletedItem)){ 
				//System.out.println("received delete measurement event, updating view!");
				updateView();
				setStatusLine("Deleted measurement successfully. ");
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