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
import measure.windperformancercp.model.query.IPerformanceQuery;
import measure.windperformancercp.model.query.MeasureIEC;
import measure.windperformancercp.model.query.MeasureLangevin;
import measure.windperformancercp.model.query.PerformanceModel;
import measure.windperformancercp.model.sources.ISource;
import measure.windperformancercp.views.IPresenter;
import measure.windperformancercp.views.performance.AssignPerformanceMeasPresenter;
import measure.windperformancercp.views.performance.PerformanceWizardDialogPresenter;
import measure.windperformancercp.views.result.ActiveQueriesPresenter;

/**
 * Does the main work for the performance measurement managing.
 * e.g.: manage performance model data (add, delete, modify)
 * provides available model data fpr the views
 * invokes query creation and connection/disconnection
 * @author Diana von Gallera
 *
 */
public class PMController implements IController {

	private static ArrayList<PerformanceWizardDialogPresenter> pwdPresenters = new ArrayList<PerformanceWizardDialogPresenter>();
	private static ArrayList<AssignPerformanceMeasPresenter> apmPresenters = new ArrayList<AssignPerformanceMeasPresenter>();
	private static ArrayList<ActiveQueriesPresenter> rpPresenters = new ArrayList<ActiveQueriesPresenter>();
	
	private static PMController instance = new PMController();
	
	IController _control;
	static SourceController scontrol;
	static PerformanceModel pmodel;
	static Connector connector;
	
	static final String addStreamParserID = "CQL";
	static final String queryParserID = "PQL";

	private PMController(){
		//System.out.println(this.toString()+": pmController says hello!");
		pmodel = PerformanceModel.getInstance();
		connector = Connector.getInstance();
	}
	
	/** 
	 * Sets the source controller. Called by main controller.
	 * @param scont The instance of the source controller.
	 */
	public static void setBrotherControl(SourceController scont){
		scontrol = scont;
		if(scontrol == null){
			//TODO Error message
		}
	}
	
	/**
	 * Adds a presenter to the list of presenters and subscribes to its events.
	 * @param <E>
	 * @param list
	 * @param pres
	 */
	public static <E extends IPresenter> void registerPresenter(ArrayList<E> list, E pres){
		pres.subscribeToAll(presenterListener);
		list.add(pres);
	}
	
	/**
	 * Deletes a presenter to the list of presenters and subscribes to its events.
	 * TODO: unsubscription. If you uncomment the lower line, the application crashes!
	 * @param <E>
	 * @param list
	 * @param pres
	 */
	public static <E extends IPresenter> void deregisterPresenter(ArrayList<E> list, E pres){
		while(list.contains(pres)){
			list.remove(pres);
		}
		//pres.unSubscribeFromAll(presenterListener); //<- if I do this, I get an exception
	}
	
	/**
	 * The main event listener for the presenters. 
	 * Invokes several actions: query creation, deletion, connection..
	 */
	public static IEventListener presenterListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> event){
			//Registration request of a presenter
			if(event.getEventType().equals(InputDialogEventType.RegisterDialog)){ 
				//System.out.println(this.toString()+": Received new dialog registry event");
			}
			//Deregistration request of a presenter
			if(event.getEventType().equals(InputDialogEventType.DeregisterDialog)){ 
				//System.out.println(this.toString()+": Received new dialog deregistry event");
				
				InputDialogEvent ideEvent = (InputDialogEvent) event;
				IPresenter pres = ideEvent.getPresenter();
							
				if(pres instanceof PerformanceWizardDialogPresenter) 
					deregisterPresenter(pwdPresenters, (PerformanceWizardDialogPresenter)pres);
				if(pres instanceof AssignPerformanceMeasPresenter)
					deregisterPresenter(apmPresenters, (AssignPerformanceMeasPresenter)pres);
				if(pres instanceof ActiveQueriesPresenter)
					deregisterPresenter(rpPresenters, (ActiveQueriesPresenter)pres);
			}
			//Add event of a performance query
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
			//Delete event of a performance query
			if(event.getEventType().equals(InputDialogEventType.DeletePerformanceItem)){
				//System.out.println(this.toString()+": Received delete performance event");
				InputDialogEvent ideEvent = (InputDialogEvent) event;
				IPerformanceQuery qry = (IPerformanceQuery) ideEvent.getValue();
				
				pmodel.removeAllOccurences(qry);
				//System.out.println(this.toString()+": Received delete source event for "+src.toString()+" from "+event.getSender().toString()+"\n Deleted "+c);
			}
			//Connection request event for a query  
			if(event.getEventType().equals(QueryEventType.AddQuery)){
					
				QueryEvent ideEvent = (QueryEvent) event;
				IPerformanceQuery qry = (IPerformanceQuery) ideEvent.getValue();
				if(!qry.getConnectStat()){
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
						
						boolean validSources = false; //indicates if sources are added properly
						//add sources
						
						if(adderQuery.equals("")){	//sources are already connected
							validSources = true;
						}
						
						if(connector.addQuery(adderQuery, addStreamParserID, new ArrayList<String>())) {	
							validSources = true;	
							//tell scontrol, that specific source has been connected
							scontrol.tellWhichAreConnected(qry.getConcernedSrcKeys());
							//TODO: maybe some of the sources are already connected (not by this application). is it possible to ask odysseus for that?
						}
						
						if(validSources){
							//add main query
							List<String> names = new ArrayList<String>();
							names.add(qry.getIdentifier());
			//maybe here a new thread could be created		
							if(connector.addQuery(qry.getQueryText(), queryParserID, names)){ //TODO: der query hinzufuegen, sie soll wissen, wer sie erzeugt hat
								qry.setConnectStat(true);
								pmodel.somethingChanged(qry);
							}
							else{
								System.out.println(this.toString()+" error at connection of measurement");
								apmPresenters.get(0).notifyUser("Connection was not successful! Error at connection.");
								//remove sources which are not responsible for other queries
							}
						} 
					}
				}
			}
			//Disconnection request event for a performance query
			if(event.getEventType().equals(QueryEventType.DeleteQuery)){
				
				QueryEvent ideEvent = (QueryEvent) event;
				IPerformanceQuery qry = (IPerformanceQuery) ideEvent.getValue();
				
				
				//remove main query
				if(connector.delQuery(qry.getIdentifier())){
						qry.setConnectStat(false);
						pmodel.somethingChanged(qry);
					//TODO: check if sources are not involved in other queries, otherwise dont delete them
					ArrayList<String> removers = qry.getStrRemQueries();
					String removerQuery = "";
					for(String s: removers){
						removerQuery = removerQuery + s;
					}
					//remove sources
					if(removerQuery != ""){
						if(connector.addQuery(removerQuery, addStreamParserID, new ArrayList<String>())){						
							scontrol.tellWhichAreDisconnected(qry.getConcernedSrcKeys());						
						}
						else{
						
						}
					}
				}
			}
		}
	};
	
	/**
	 * Returns the content of the own model (performance model)
	 */
	@Override
	public ArrayList<?> getContent() {
		return pmodel.getQueryList();
	}
	
	/**
	 * 
	 * @return The content of the source model
	 */
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
		if(pres instanceof ActiveQueriesPresenter) 
			registerPresenter(rpPresenters, (ActiveQueriesPresenter) pres);
		return instance;
	}
}
