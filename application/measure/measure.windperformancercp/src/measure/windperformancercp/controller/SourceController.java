package measure.windperformancercp.controller;

import java.util.ArrayList;

import measure.windperformancercp.event.IEvent;
import measure.windperformancercp.event.IEventListener;
import measure.windperformancercp.event.InputDialogEvent;
import measure.windperformancercp.event.InputDialogEventType;
import measure.windperformancercp.model.sources.ISource;
import measure.windperformancercp.model.sources.MetMast;
import measure.windperformancercp.model.sources.SourceModel;
import measure.windperformancercp.model.sources.WindTurbine;
import measure.windperformancercp.views.IPresenter;

import measure.windperformancercp.views.sources.ManageSourcePresenter;
import measure.windperformancercp.views.sources.SourceDialogPresenter;

public class SourceController implements IController {
	
	private static SourceController instance = new SourceController();
	private static ArrayList<ManageSourcePresenter> msrcPresenters = new ArrayList<ManageSourcePresenter>();
	private static ArrayList<SourceDialogPresenter> srcDPresenters = new ArrayList<SourceDialogPresenter>();
	
	IController _control;
	static PMController pcontrol;
	static SourceModel model;
	

	private SourceController(){
		//System.out.println(this.toString()+": sourceController says hello!");
		model = SourceModel.getInstance();
	}
	
	public static void setBrotherControl(PMController pcont){
		pcontrol = pcont;
		if(pcontrol == null){
			//TODO Error message
		}
	}
	
	public static <E extends IPresenter> void registerPresenter(ArrayList<E> list, E pres){
		pres.subscribeToAll(presenterListener);
		list.add(pres);
	}
	
	
	public static <E extends IPresenter> void deregisterPresenter(ArrayList<E> list, E pres){
		while(list.contains(pres)){
			list.remove(pres);
		}
		//pres.unSubscribeFromAll(presenterListener);
	}
	
//source Model modification wird hier ausgefuehrt
	public static IEventListener presenterListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> event){
			if(event.getEventType().equals(InputDialogEventType.RegisterDialog)){ 
				//System.out.println(this.toString()+": Received new dialog registry event");
			}
			
			if(event.getEventType().equals(InputDialogEventType.DeregisterDialog)){ 
				//System.out.println(this.toString()+": Received new dialog deregistry event");
				
				InputDialogEvent ideEvent = (InputDialogEvent) event;
				IPresenter pres = ideEvent.getPresenter();
							
				if(pres instanceof ManageSourcePresenter) 
					deregisterPresenter(msrcPresenters, (ManageSourcePresenter)pres);
				if(pres instanceof SourceDialogPresenter)
					deregisterPresenter(srcDPresenters, (SourceDialogPresenter)pres);
			}
			
			if(event.getEventType().equals(InputDialogEventType.NewSourceItem)){
				//System.out.println(this.toString()+": Received new source event");
				InputDialogEvent ideEvent = (InputDialogEvent) event;
				ISource src = (ISource) ideEvent.getValue();
				if(src instanceof WindTurbine){
					ISource newsrc = new WindTurbine((WindTurbine)src);
					model.addElement(newsrc);
				}
				if(src instanceof MetMast){
					ISource newsrc = new MetMast((MetMast)src);
					model.addElement(newsrc);
				}
								
				
			}
			if(event.getEventType().equals(InputDialogEventType.DeleteSourceItem)){
				
				InputDialogEvent ideEvent = (InputDialogEvent) event;
				ISource src = (ISource) ideEvent.getValue();
				int c = model.removeAllOccurences(src);				
				//System.out.println(this.toString()+": Received delete source event for "+src.toString()+" from "+event.getSender().toString()+"\n Deleted "+c);
			}			
		}
	};
	
	

	@Override
	public ArrayList<ISource> getContent(){
		return model.getSourcesList();
	}
	
	public static <E extends IPresenter> SourceController getInstance(E pres){
		if(pres instanceof SourceDialogPresenter) 
			registerPresenter(srcDPresenters, (SourceDialogPresenter) pres);
		if(pres instanceof ManageSourcePresenter)
			registerPresenter(msrcPresenters, (ManageSourcePresenter) pres);
		return instance;
	}

	
	public static SourceController getInstance(){
		return instance;
	}


	
} //end_SourceControler
