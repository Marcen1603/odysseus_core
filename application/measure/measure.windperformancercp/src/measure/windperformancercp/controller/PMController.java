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

import measure.windperformancercp.event.IEvent;
import measure.windperformancercp.event.IEventListener;
import measure.windperformancercp.event.InputDialogEvent;
import measure.windperformancercp.event.InputDialogEventType;
import measure.windperformancercp.event.ModelEventType;
import measure.windperformancercp.event.QueryEvent;
import measure.windperformancercp.event.QueryEventType;
import measure.windperformancercp.model.query.IPerformanceQuery;
import measure.windperformancercp.model.query.MeasureIEC;
import measure.windperformancercp.model.query.MeasureLangevin;
import measure.windperformancercp.model.query.PerformanceModel;
import measure.windperformancercp.model.query.QueryGenerator;
import measure.windperformancercp.model.sources.ISource;
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
				ArrayList<String> adders = qry.generateSourceStreams();
				//TODO:check if sources are not already registered from other queries
				String adderQuery = "";
			
				for(String s: adders){
					adderQuery = adderQuery + s;
				}
				//add sources
				if(connector.addQuery(adderQuery, addStreamParserID)) {
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
			
			if(event.getEventType().equals(QueryEventType.DeleteQuery)){
				QueryEvent ideEvent = (QueryEvent) event;
				IPerformanceQuery qry = (IPerformanceQuery) ideEvent.getValue();
				
				
				//remove main query
				//if(connector.delQuery(queryID)){
						//qry.setConnectStat(false);
						//pmodel.somethingChanged(qry);
					//check if sources are not involved in other queries, otherwise dont delete them
					ArrayList<String> removers = qry.generateRemoveStreams();
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
