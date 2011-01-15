package windperformancercp.controller;

import java.util.ArrayList;

import windperformancercp.model.query.CQLGenerator;
import windperformancercp.model.query.OperatorResult;
import windperformancercp.model.query.PQLGenerator;
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
	SourceController scontrol;
	QueryGenerator gen;

	private PMController(){
		scontrol = SourceController.getInstance();
		//System.out.println(this.toString()+": sourceController says hello!");
		//model = SourceModel.getInstance();
		//gen = new QueryGenerator();

	}

	public String callGen(ISource src){
			
		QueryGenerator gen = new QueryGenerator(new CQLGenerator());
		OperatorResult res1 = gen.generateCreateStream(src);
		OperatorResult res2 = gen.generateRemoveStream(src);
		QueryGenerator gen2 = new QueryGenerator(new PQLGenerator());
		
		//Aggregation agg = gen2.new Aggregation("AVG",res1.getStream().getIthAttName(0),"avg"+res1.getStream().getIthAttName(0));
		
		//OperatorResult res3 = gen2.generateAggregation(res1.getStream(), new String[]{res1.getStream().getIthAttName(1)}, new Aggregation[]{agg}, "aggregated");
		//OperatorResult res3 = gen2.generateRename(res1.getStream(), new String[]{"babbel","wabbel"}, "renamed");
		//OperatorResult res3 = gen2.generateMap(res1.getStream(), new String[]{"add","mult"}, new String[]{res1.getStream().getAttributes().get(0)+"+"+res1.getStream().getAttributes().get(1),res1.getStream().getAttributes().get(0)+"*"+res1.getStream().getAttributes().get(1)}, "mapped");
		OperatorResult res3 = gen2.generateSelection(res1.getStream(),res1.getStream().getIthAttName(0)+">"+2.0+" AND "+res1.getStream().getIthAttName(0)+"<"+19.0,"selected");
		
		return  res1.getQuery()+res2.getQuery()+"\n"+res3.getQuery();
		
	}
	
	@Override
	public ArrayList<?> getContent() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArrayList<?> getAvailableSources(){
		return scontrol.getContent();
	}
	
	public static PMController getInstance(){
		return instance;
	}
	
	public static <E extends IPresenter> PMController getInstance(E pres){
		//if(pres instanceof PerformanceWizardDialogPresenter) 
			//registerPresenter(pwdPresenters, (PerformanceWizardDialogPresenter) pres);
		//if(pres instanceof AssignPerformanceMeasPresenter) 
			//registerPresenter(pwdPresenters, (AssignPerformanceMeasPresenter) pres);
		return instance;
	}
}
