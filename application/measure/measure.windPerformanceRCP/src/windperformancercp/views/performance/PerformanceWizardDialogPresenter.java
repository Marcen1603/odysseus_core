package windperformancercp.views.performance;

import java.util.ArrayList;

import windperformancercp.controller.IController;
import windperformancercp.controller.PMController;
import windperformancercp.event.EventHandler;
import windperformancercp.event.IEvent;
import windperformancercp.event.IEventListener;
import windperformancercp.event.InputDialogEvent;
import windperformancercp.event.InputDialogEventType;
import windperformancercp.model.query.APerformanceQuery;
import windperformancercp.model.query.IPerformanceQuery;
import windperformancercp.model.sources.IDialogResult;
import windperformancercp.model.sources.ISource;
import windperformancercp.views.IPresenter;

public class PerformanceWizardDialogPresenter extends EventHandler implements IPresenter{
	
	QueryWizardDialog dialog;
	IPerformanceQuery query;
	

	//ArrayList<Attribute> tmpAttList;
	IController _cont;
	final PerformanceWizardDialogPresenter boss;

	public PerformanceWizardDialogPresenter(QueryWizardDialog caller){
		boss = this;
		//System.out.println(this.toString()+": source dialog presenter says hi!");
		dialog = caller;
		_cont = PMController.getInstance(this);
		

		fire(new InputDialogEvent(this,InputDialogEventType.RegisterDialog,null));
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
		
	public void finishClick(){
		dialog.close();
		fire(new InputDialogEvent(this,InputDialogEventType.DeregisterDialog,null));
	}

	@Override
	public void feedDialog(IDialogResult input) {
		
	}
	
	public Object[] getAvailableMethods(){
		return APerformanceQuery.PMType.values();
	}
	
	public ArrayList<String> getAvailableSources(){
		ArrayList<ISource> sources = ((PMController)_cont).getAvailableSources();
		ArrayList<String> sourceNames = new ArrayList<String>(); 
		for(ISource o: sources){
			sourceNames.add(o.getName()+"@"+o.getHost()+":"+o.getPort());
		}
		return sourceNames;
	}
	
}
