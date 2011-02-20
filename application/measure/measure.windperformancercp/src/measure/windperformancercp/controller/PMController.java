/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package measure.windperformancercp.controller;

import java.util.ArrayList;
import java.util.List;

import measure.windperformancercp.event.IEvent;
import measure.windperformancercp.event.IEventListener;
import measure.windperformancercp.event.InputDialogEvent;
import measure.windperformancercp.event.InputDialogEventType;
import measure.windperformancercp.event.QueryEvent;
import measure.windperformancercp.event.QueryEventType;
import measure.windperformancercp.model.query.Assignment;
import measure.windperformancercp.model.query.IPerformanceQuery;
import measure.windperformancercp.model.query.MeasureIEC;
import measure.windperformancercp.model.query.MeasureLangevin;
import measure.windperformancercp.model.query.OperatorResult;
import measure.windperformancercp.model.query.PerformanceModel;
import measure.windperformancercp.model.query.QueryGenerator;
import measure.windperformancercp.model.query.Stream;
import measure.windperformancercp.model.sources.Attribute;
import measure.windperformancercp.model.sources.Attribute.AttributeType;
import measure.windperformancercp.model.sources.ISource;
import measure.windperformancercp.model.sources.WindTurbine;
import measure.windperformancercp.views.IPresenter;
import measure.windperformancercp.views.performance.AssignPerformanceMeasPresenter;
import measure.windperformancercp.views.performance.PerformanceWizardDialogPresenter;

public class PMController implements IController {

	private static ArrayList<PerformanceWizardDialogPresenter> pwdPresenters = new ArrayList<PerformanceWizardDialogPresenter>();
	private static ArrayList<AssignPerformanceMeasPresenter> apmPresenters = new ArrayList<AssignPerformanceMeasPresenter>();
	
	private static PMController instance = new PMController();
	
	IController _control;
	static SourceController scontrol;
	static PerformanceModel pmodel;
	static QueryGenerator gen;
	static Connector connector;
	
	static final String addStreamParserID = "CQL";
	static final String queryParserID = "PQL";

	private PMController(){
		//System.out.println(this.toString()+": pmController says hello!");
		pmodel = PerformanceModel.getInstance();
		connector = Connector.getInstance();
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
			
			if(event.getEventType().equals(QueryEventType.AddQuery)){
					
				QueryEvent ideEvent = (QueryEvent) event;
				IPerformanceQuery qry = (IPerformanceQuery) ideEvent.getValue();
				
				//check if sources are not already registered from other queries
				List<Boolean> connectStates = scontrol.getConnectionList(qry.getConcernedSrcKeys());
				List<String> adders = qry.getStrGenQueries();
				//generate list of access queries for the disconnected sources
				String adderQuery = "";
				int j = connectStates.size();
				//for every source the information must be set, whether it is connected or not
				if(j == adders.size()){					
					for(int i=0; i<j; i++){
						adderQuery = adderQuery + ((!connectStates.get(i))? adders.get(i): "");
					}
					//add sources
					if(connector.addQuery(adderQuery, addStreamParserID)) {	
							
						
						scontrol.tellWhichAreConnected(qry.getConcernedSrcKeys());
						for(ISource bla: scontrol.getContent()){
							System.out.println(this.toString()+" smodel :"+bla.toString()+bla.hashCode());
						}
						for(ISource bla: scontrol.getSourcesFromModel(qry.getConcernedSrcKeys())){
							System.out.println(this.toString()+"qrysrc :"+bla.toString()+bla.hashCode());
						}


						//TODO: maybe some of the sources are already connected (not by this application). is it possible to ask odysseus for that?
						//TODO: tell scontrol, that specific source has been connected
						
						//scontrol.somethingChanged();
						//add main query
						if(connector.addQuery(qry.getQueryText(), queryParserID)){ //TODO: der query hinzufuegen, sie soll wissen, wer sie erzeugt hat
							qry.setConnectStat(true);
							pmodel.somethingChanged(qry);
						}
						else{
							//remove sources
						}
					}
				}
				
		
			}
			
			if(event.getEventType().equals(QueryEventType.DeleteQuery)){
				
				QueryEvent ideEvent = (QueryEvent) event;
				IPerformanceQuery qry = (IPerformanceQuery) ideEvent.getValue();
				
				
				//remove main query
				//if(connector.delQuery(queryID)){
						//qry.setConnectStat(false);
						//pmodel.somethingChanged(qry);
					//check if sources are not involved in other queries, otherwise dont delete them
					ArrayList<String> removers = qry.getStrRemQueries();
					String removerQuery = "";
					for(String s: removers){
						removerQuery = removerQuery + s;
					}
					//remove sources
					if(connector.addQuery(removerQuery, addStreamParserID)){
						//TODO: tell scontrol, that a source has been disconnected
					}
					else{
						
					}
			//	}
				
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
	
	
/*  CODE FROM APERFORMANCE QUERY */
	/**
	 * Sets queries turbine data 
	 */
	public void extractTurbineDataForQuery(IPerformanceQuery query){
		for(ISource src: scontrol.getSourcesFromModel(query.getConcernedSrcKeys())){
			if(src.isWindTurbine()){				
				query.setPitch(((WindTurbine)src).getPowerControl()==0);
				break;
			}
		}
	}
	
	//TODO: auf namen der timestamps ändern.
	public void updateTimestampAttributes(IPerformanceQuery query){
		boolean set = false;
		
		ArrayList<Integer> timestampPos= new ArrayList<Integer>();
		//timestampAttributes.clear();
		//for(int i =0; i< concernedSrc.size();i++){
		for(ISource src: scontrol.getSourcesFromModel(query.getConcernedSrcKeys())){
			ArrayList<Attribute> atts = src.getAttributeList();
			set = false;
			for(int j = 0; j<atts.size();j++){
				Attribute att = atts.get(j);
				if(att.getAttType().equals(AttributeType.STARTTIMESTAMP)){
					timestampPos.add(j);
					set = true;
					break;
				}
			}
			if(!set)
				timestampPos.add(0);
		}
		query.setTimestampAttributes(timestampPos);
	}
	
	
	public void generateSourceStreams(IPerformanceQuery query){
		ArrayList<String> qryresult = new ArrayList<String>(); 
		ArrayList<Stream> inputStreams = new ArrayList<Stream>();
		//concernedStr.clear();
		for(ISource src: scontrol.getSourcesFromModel(query.getConcernedSrcKeys())){
			OperatorResult current = query.getSourceCreator().generateCreateStream(src);
			if(current != null){
				
				for(Assignment as: query.getAssignments()){
					if(as.getRespSource().equals(src))
						as.setRespStream(current.getStream());
				}
				inputStreams.add(current.getStream());
				qryresult.add(current.getQuery());
			}
			else{
				//TODO: Fehlermeldung
			}
	//		System.out.println(current.getQuery());
		}
		query.setStrGenQueries(qryresult);
		query.setConcernedStr(inputStreams);
	}
	
	
	//evtl auch auf void umändern
	public ArrayList<String> generateRemoveStreams(IPerformanceQuery query){
		ArrayList<String> qryresult = new ArrayList<String>(); 
	//	concernedStr.clear(); //nicht so sinnvoll, oder?
		for(ISource src: scontrol.getSourcesFromModel(query.getConcernedSrcKeys())){
			OperatorResult current = query.getSourceCreator().generateRemoveStream(src);
			if(current != null){
				qryresult.add(current.getQuery());
			}
			else{
				//TODO: Fehlermeldung
			}
	//		System.out.println(current.getQuery());
		}
		query.setStrRemQueries(qryresult);
		return qryresult;
	}
	
	public ArrayList<Assignment> getPossibleAssignments(IPerformanceQuery query){
		ArrayList<Assignment> possibilities = new ArrayList<Assignment>();
		for(Assignment a: query.getAssignments()){
			for(ISource s:  scontrol.getSourcesFromModel(query.getConcernedSrcKeys())){
				for(Attribute at: s.getAttributeList()){
					if(a.getAttType().equals(at.getAttType())){
						Assignment newpos = new Assignment(a.getAttType(),s,s.getAttIndex(at));
						possibilities.add(newpos);
					}
				}
			}
		}
			
		return possibilities;
	}
	
	
	//END Query Code
	
	
	
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
