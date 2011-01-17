package windperformancercp.views.performance;

import java.util.ArrayList;

import windperformancercp.controller.IController;
import windperformancercp.controller.PMController;
import windperformancercp.event.EventHandler;
import windperformancercp.event.InputDialogEvent;
import windperformancercp.event.InputDialogEventType;
import windperformancercp.model.query.APerformanceQuery;
import windperformancercp.model.query.Assignment;
import windperformancercp.model.query.IPerformanceQuery;
import windperformancercp.model.query.MeasureIEC;
import windperformancercp.model.query.MeasureLangevin;
import windperformancercp.model.sources.Attribute;
import windperformancercp.model.sources.IDialogResult;
import windperformancercp.model.sources.ISource;
import windperformancercp.views.IPresenter;

public class PerformanceWizardDialogPresenter extends EventHandler implements IPresenter{
	
	QueryWizardDialog dialog;
	IPerformanceQuery query;
	ArrayList<SourceNameTuple> srcnT;
	IController _cont;
	ArrayList<ArrayList<String>> restructPAssign2;
	final PerformanceWizardDialogPresenter boss;
	

	private class SourceNameTuple{
		public ISource src;
		public String name;
		
		public SourceNameTuple(ISource s, String n){
			this.src = s;
			this.name = n;
		}
	}

	public PerformanceWizardDialogPresenter(QueryWizardDialog caller){
		boss = this;
		//System.out.println(this.toString()+": source dialog presenter says hi!");
		dialog = caller;
		srcnT = new ArrayList<SourceNameTuple>();
		restructPAssign2 = new ArrayList<ArrayList<String>>();
		_cont = PMController.getInstance(this);
		fire(new InputDialogEvent(this,InputDialogEventType.RegisterDialog,null));
	}

	/*IEventListener attListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> idevent){
			/*
				InputDialogEvent newAttevent = (InputDialogEvent) idevent;
				Attribute att = (Attribute)newAttevent.getValue();
			//	fire(new InputDialogEvent(boss, InputDialogEventType.NewAttributeItem, att));
				tmpAttList.add(att);
				if(source != null)
					source.setAttributeList(tmpAttList);
				updateDialog();
			//}
		}
	};*/
		
	public void finishClick(){
		dialog.close();
		fire(new InputDialogEvent(this,InputDialogEventType.DeregisterDialog,null));
	}
	
	public void typeChoosed(String id, String method){
		APerformanceQuery.PMType type = APerformanceQuery.PMType.valueOf(method);
		if(type.equals(APerformanceQuery.PMType.IEC)) 
			query = new MeasureIEC(id);
		if(type.equals(APerformanceQuery.PMType.Langevin))
			query= new MeasureLangevin(id);
	}
	
	public void sourcesChoosed(ArrayList<String> sources){
		if(query != null){			
			query.clearMembers();
			
			for(String s:sources){
				for(SourceNameTuple t: srcnT){
					if(t.name.equals(s)){
						query.addMember(t.src);
					}
				}
			}
			
			updatePossibleAssignments();
			//query.generateSourceStreams();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void updatePossibleAssignments(){
		if(query != null){
			ArrayList<Assignment> possAssign = query.getPossibleAssignments();
			ArrayList<Assignment> neededAssign = query.getAssignments(null);
			ArrayList<String>[] restructPAssign = new ArrayList[neededAssign.size()];			
	
			for(int i = 0; i< restructPAssign.length; i++){
				Attribute.AttributeType neededType = neededAssign.get(i).getAttType();
				restructPAssign[i] = new ArrayList<String>();
		
				for(Assignment a: possAssign){
					if(a.getAttType().equals(neededType)){
						restructPAssign[i].add(a.toString());
					}
				}
				restructPAssign2.add(restructPAssign[i]);
			}
		}
	
	}

	@Override
	public void feedDialog(IDialogResult input) {
		
	}
	
	public Object[] getAvailableMethods(){
		return APerformanceQuery.PMType.values();
	}
	
	public ArrayList<String> getAvailableSources(){
		ArrayList<ISource> sources = ((PMController)_cont).getAvailableSources();
		//what should be visible in the left listViewer
		ArrayList<String> sourceNames = new ArrayList<String>();
		srcnT.clear();
		
		for(ISource o: sources){
			String name = o.getName()+"@"+o.getHost()+":"+o.getPort();
			sourceNames.add(name);
			SourceNameTuple tuple = new SourceNameTuple(o,name);
			//for later lookup
			srcnT.add(tuple);
		}
		return sourceNames;
	}
	
	public ArrayList<String> getNeededAssignments(String forWhat){
		ArrayList<String> result = new ArrayList<String>();
		for(Assignment a:query.getAssignments(null)){
			result.add(a.getAttType().toString());
		}
		return result; 
	}
	
	public ArrayList<ArrayList<String>> getPossibleAssignments(){
		return restructPAssign2; 
	}
	

	
}
