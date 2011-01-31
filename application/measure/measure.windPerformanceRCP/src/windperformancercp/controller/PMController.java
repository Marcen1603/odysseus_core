package windperformancercp.controller;

import java.util.ArrayList;

import windperformancercp.event.IEvent;
import windperformancercp.event.IEventListener;
import windperformancercp.event.InputDialogEvent;
import windperformancercp.event.InputDialogEventType;
import windperformancercp.model.query.IPerformanceQuery;
import windperformancercp.model.query.MeasureIEC;
import windperformancercp.model.query.MeasureLangevin;
import windperformancercp.model.query.PerformanceModel;
import windperformancercp.model.query.QueryGenerator;
import windperformancercp.model.sources.ISource;
import windperformancercp.views.IPresenter;
import windperformancercp.views.performance.AssignPerformanceMeasPresenter;
import windperformancercp.views.performance.PerformanceWizardDialogPresenter;

public class PMController implements IController {

	private static ArrayList<PerformanceWizardDialogPresenter> pwdPresenters = new ArrayList<PerformanceWizardDialogPresenter>();
	private static ArrayList<AssignPerformanceMeasPresenter> apmPresenters = new ArrayList<AssignPerformanceMeasPresenter>();
	
	private static PMController instance = new PMController();
	
	IController _control;
	static SourceController scontrol;
	static PerformanceModel pmodel;
	QueryGenerator gen;
	Connector connector;

	private PMController(){
		//System.out.println(this.toString()+": pmController says hello!");
		pmodel = PerformanceModel.getInstance();
		connector = new Connector();
	}
	
	public static void setBrotherControl(SourceController scont){
		scontrol = scont;
		if(scontrol == null){
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
		//pres.unSubscribeFromAll(presenterListener); //<- if I do this, I get an exception
	}
	
	public static IEventListener presenterListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> event){
			if(event.getEventType().equals(InputDialogEventType.RegisterDialog)){ 
				//System.out.println(this.toString()+": Received new dialog registry event");
			}
			
			if(event.getEventType().equals(InputDialogEventType.DeregisterDialog)){ 
				//System.out.println(this.toString()+": Received new dialog deregistry event");
				
				InputDialogEvent ideEvent = (InputDialogEvent) event;
				IPresenter pres = ideEvent.getPresenter();
							
				if(pres instanceof PerformanceWizardDialogPresenter) 
					deregisterPresenter(pwdPresenters, (PerformanceWizardDialogPresenter)pres);
				if(pres instanceof AssignPerformanceMeasPresenter)
					deregisterPresenter(apmPresenters, (AssignPerformanceMeasPresenter)pres);
			}
			
			if(event.getEventType().equals(InputDialogEventType.NewPerformanceItem)){
				//System.out.println(this.toString()+": Received new performance event");
				InputDialogEvent ideEvent = (InputDialogEvent) event;
				IPerformanceQuery qry = (IPerformanceQuery) ideEvent.getValue(); 
	
				if(qry instanceof MeasureIEC){
					IPerformanceQuery newqry = new MeasureIEC((MeasureIEC)qry);
					pmodel.addElement(newqry);
				}
				if(qry instanceof MeasureLangevin){
					IPerformanceQuery newqry = new MeasureLangevin((MeasureLangevin)qry);
					pmodel.addElement(newqry);
				}
				
			}
			if(event.getEventType().equals(InputDialogEventType.DeletePerformanceItem)){
				//System.out.println(this.toString()+": Received delete performance event");
				InputDialogEvent ideEvent = (InputDialogEvent) event;
				IPerformanceQuery qry = (IPerformanceQuery) ideEvent.getValue();
				
				//int c = pmodel.removeAllOccurences(qry);				
				pmodel.removeAllOccurences(qry);
				//System.out.println(this.toString()+": Received delete source event for "+src.toString()+" from "+event.getSender().toString()+"\n Deleted "+c);
			}			
		}
	};
	
	@Override
	public ArrayList<?> getContent() {
		return pmodel.getQueryList();
	}
	
	public ArrayList<ISource> getAvailableSources(){
		ArrayList<ISource> src = scontrol.getContent();
		if(src != null)
			return src;
		else
			return new ArrayList<ISource>();
	}
	
	
	public static PMController getInstance(){
		return instance;
	}
	
	public static <E extends IPresenter> PMController getInstance(E pres){
		if(pres instanceof PerformanceWizardDialogPresenter) 
			registerPresenter(pwdPresenters, (PerformanceWizardDialogPresenter) pres);
		if(pres instanceof AssignPerformanceMeasPresenter) 
			registerPresenter(apmPresenters, (AssignPerformanceMeasPresenter) pres);
		return instance;
	}
}
