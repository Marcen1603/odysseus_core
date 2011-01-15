package windperformancercp.views.performance;

import java.util.ArrayList;

import windperformancercp.controller.IController;
import windperformancercp.controller.PMController;
import windperformancercp.event.EventHandler;
import windperformancercp.event.IEvent;
import windperformancercp.event.IEventListener;
import windperformancercp.model.query.IPerformanceQuery;
import windperformancercp.model.sources.Attribute;
import windperformancercp.model.sources.IDialogResult;
import windperformancercp.views.IPresenter;

public class PerformanceWizardDialogPresenter extends EventHandler implements IPresenter{
	
	QueryWizardDialog dialog;
	IPerformanceQuery query;

	//ArrayList<Attribute> tmpAttList;
	IController _cont;
	final PerformanceWizardDialogPresenter boss;

	//TODO: auslagern
	int MMId = 0;
	int WTId = 1;
	
	public PerformanceWizardDialogPresenter(QueryWizardDialog caller){
		boss = this;
		//System.out.println(this.toString()+": source dialog presenter says hi!");
		dialog = caller;
		_cont = PMController.getInstance(this);

		//fire(new InputDialogEvent(this,InputDialogEventType.RegisterDialog,null));
	}

	IEventListener attListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> idevent){
			/*
				InputDialogEvent newAttevent = (InputDialogEvent) idevent;
				Attribute att = (Attribute)newAttevent.getValue();
			//	fire(new InputDialogEvent(boss, InputDialogEventType.NewAttributeItem, att));
				tmpAttList.add(att);
				if(source != null)
					source.setAttributeList(tmpAttList);
				updateDialog();
			//}*/
		}
	};
	
	
	public void nameEntered(){
		
	}
	
	public void performanceMeasureTypeClick(){
		
	}
	
	public void sourceSelectionClick(){
		
	}
	
	public void attributeAssignmentClick(){
		
	}

	@Override
	public void feedDialog(IDialogResult input) {
		// TODO Auto-generated method stub
		
	}
	
}
