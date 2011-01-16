package windperformancercp.controller;

import java.util.ArrayList;

import windperformancercp.event.IEvent;
import windperformancercp.event.IEventListener;
import windperformancercp.event.InputDialogEvent;
import windperformancercp.event.InputDialogEventType;
import windperformancercp.model.query.CQLGenerator;
import windperformancercp.model.query.IPerformanceQuery;
import windperformancercp.model.query.OperatorResult;
import windperformancercp.model.query.PQLGenerator;
import windperformancercp.model.query.PerformanceModel;
import windperformancercp.model.query.QueryGenerator;
import windperformancercp.model.query.Stream;
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
	

	private PMController(){
		
		//System.out.println(this.toString()+": pmController says hello!");
		pmodel = PerformanceModel.getInstance();
		
		//gen = new QueryGenerator();
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
		//pres.unSubscribeFromAll(presenterListener);
	}
	
	public static IEventListener presenterListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> event){
			if(event.getEventType().equals(InputDialogEventType.RegisterDialog)){ 
				System.out.println(this.toString()+": Received new dialog registry event");
			}
			
			if(event.getEventType().equals(InputDialogEventType.DeregisterDialog)){ 
				System.out.println(this.toString()+": Received new dialog deregistry event");
				
				InputDialogEvent ideEvent = (InputDialogEvent) event;
				IPresenter pres = ideEvent.getPresenter();
							
				if(pres instanceof PerformanceWizardDialogPresenter) 
					deregisterPresenter(pwdPresenters, (PerformanceWizardDialogPresenter)pres);
				if(pres instanceof AssignPerformanceMeasPresenter)
					deregisterPresenter(apmPresenters, (AssignPerformanceMeasPresenter)pres);
			}
			
			if(event.getEventType().equals(InputDialogEventType.NewPerformanceItem)){
				System.out.println(this.toString()+": Received new performance event");
				InputDialogEvent ideEvent = (InputDialogEvent) event;
				IPerformanceQuery qry = (IPerformanceQuery) ideEvent.getValue(); 
			
				
				pmodel.addElement(qry);
				//System.out.println(pcontrol.callGen(src));
	//			JAXB.marshal(src, System.out);
				
			}
			if(event.getEventType().equals(InputDialogEventType.DeletePerformanceItem)){
				System.out.println(this.toString()+": Received delete performance event");
				InputDialogEvent ideEvent = (InputDialogEvent) event;
				IPerformanceQuery qry = (IPerformanceQuery) ideEvent.getValue();
				
				int c = pmodel.removeAllOccurences(qry);				
				//System.out.println(this.toString()+": Received delete source event for "+src.toString()+" from "+event.getSender().toString()+"\n Deleted "+c);
			}			
		}
	};
	


	public String callGen(ISource src){
			
		QueryGenerator gen = new QueryGenerator(new CQLGenerator());
		OperatorResult res1 = gen.generateCreateStream(src);
		OperatorResult res2 = gen.generateRemoveStream(src);
		QueryGenerator gen2 = new QueryGenerator(new PQLGenerator());
		
		ArrayList<Stream> strList = new ArrayList<Stream>();
		strList.add(res1.getStream());
		strList.add(res1.getStream());
		//Aggregation agg = gen2.new Aggregation("AVG",res1.getStream().getIthAttName(0),"avg"+res1.getStream().getIthAttName(0));
		
		//OperatorResult res3 = gen2.generateAggregation(res1.getStream(), new String[]{res1.getStream().getIthAttName(1)}, new Aggregation[]{agg}, "aggregated");
		//OperatorResult res3 = gen2.generateRename(res1.getStream(), new String[]{"babbel","wabbel"}, "renamed");
		//OperatorResult res3 = gen2.generateMap(res1.getStream(), new String[]{"add","mult"}, new String[]{res1.getStream().getAttributes().get(0)+"+"+res1.getStream().getAttributes().get(1),res1.getStream().getAttributes().get(0)+"*"+res1.getStream().getAttributes().get(1)}, "mapped");
		//OperatorResult res3 = gen2.generateSelection(res1.getStream(),res1.getStream().getIthAttName(0)+">"+2.0+" AND "+res1.getStream().getIthAttName(0)+"<"+19.0,"selected");
		
		OperatorResult res3 = gen2.generateJoin(strList, res1.getStream().getIthAttName(0)+"="+res1.getStream().getIthAttName(0), "joined");
		
		return  res1.getQuery()+res2.getQuery()+"\n"+res3.getQuery();
		
	}
	
	@Override
	public ArrayList<?> getContent() {
		// TODO Auto-generated method stub
		return null;
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
