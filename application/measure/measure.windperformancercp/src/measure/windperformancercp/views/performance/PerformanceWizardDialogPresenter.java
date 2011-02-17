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
package measure.windperformancercp.views.performance;

import java.util.ArrayList;

import measure.windperformancercp.controller.IController;
import measure.windperformancercp.controller.PMController;
import measure.windperformancercp.event.EventHandler;
import measure.windperformancercp.event.InputDialogEvent;
import measure.windperformancercp.event.InputDialogEventType;
import measure.windperformancercp.event.UpdateEvent;
import measure.windperformancercp.event.UpdateEventType;
import measure.windperformancercp.model.query.APerformanceQuery;
import measure.windperformancercp.model.query.Assignment;
import measure.windperformancercp.model.query.IPerformanceQuery;
import measure.windperformancercp.model.query.MeasureIEC;
import measure.windperformancercp.model.query.MeasureLangevin;
import measure.windperformancercp.model.sources.Attribute;
import measure.windperformancercp.model.sources.IDialogResult;
import measure.windperformancercp.model.sources.ISource;
import measure.windperformancercp.views.IPresenter;

public class PerformanceWizardDialogPresenter extends EventHandler implements IPresenter{
	
	PerformanceWizardDialog dialog;
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

	/**
	 * Controls the <code>PerformanceWizardDialog</code>, 
	 * forwards instructions from <code>PMController</code> and data requests from Dialog.
	 * @param caller The <code>PerformanceWizardDialog</code> to control
	 */
	public PerformanceWizardDialogPresenter(PerformanceWizardDialog caller){
		boss = this;
		//System.out.println(this.toString()+": source dialog presenter says hi!");
		dialog = caller;
		srcnT = new ArrayList<SourceNameTuple>();
		restructPAssign2 = new ArrayList<ArrayList<String>>();
		_cont = PMController.getInstance(this);
		fire(new InputDialogEvent(this,InputDialogEventType.RegisterDialog,null));
	}
	
	/**
	 * Called if type of query is chosen, creates a new method/query
	 * @param id The String identifier of the query 
	 * @param method The <code>PMType</code> of the query 
	 */
	public void typeChoosed(String id, String method){
		APerformanceQuery.PMType type = APerformanceQuery.PMType.valueOf(method);
		if(type.equals(APerformanceQuery.PMType.IEC)) 
			query = new MeasureIEC(id);
		if(type.equals(APerformanceQuery.PMType.Langevin))
			query= new MeasureLangevin(id);
	}
	
	/**
	 * Called if concerned sources are chosen, 
	 * adds them to the query and invokes the update of the possible assignments
	 * @param sources
	 */
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
		}
	}
	
	/**
	 * Called if assignments are made, allocates them to the queries needed assignments.
	 * Additional, the tau and frequency values are set if query type is langevin
	 * The frequency is the maximum of all involved sources.
	 * @param dialogAssigns
	 */
	public void assignmentsMade(ArrayList<String> dialogAssigns, int tau){
		ArrayList<Assignment> finalAssignments = new ArrayList<Assignment>(); 
		if(query != null){
			
			if(!query.getPossibleAssignments().isEmpty()){
				for(Assignment queryAssign: query.getPossibleAssignments()){
					for(String entry: dialogAssigns){
						if(queryAssign.toString().equals(entry)){
							finalAssignments.add(queryAssign);
							break;
						}
					}
				}
				query.setAssignments(finalAssignments);
				query.extractSourcesFromAssignments();
			}
			if(query instanceof MeasureLangevin){
				((MeasureLangevin)query).setTau(tau);
				int frequency = 0;
				
				for(ISource src : query.getConcernedSrc()){
					if(src.getFrequency() > frequency)
						frequency = src.getFrequency();
				}
				if(frequency<1){
					//TODO: Error!
				}
					
				((MeasureLangevin)query).setFrequency(frequency);
			}
		}
	}

	/**
	 * Finalizes the query dedication and fires a new performance item event if query is not null 
	 */
	public void finishClick(){
		
		//for(String s: query.generateSourceStreams())
			//System.out.println(s);
		//System.out.println(query.generateQuery());
		query.generateSourceStreams();
		query.generateQuery();
		if(query != null){
			fire(new InputDialogEvent(this, InputDialogEventType.NewPerformanceItem, query));
			dialog.close();
		}

		fire(new InputDialogEvent(this,InputDialogEventType.DeregisterDialog,null));
	}

	/**
	 * Allocates the assignments to the combo elements
	 */
	@SuppressWarnings("unchecked")
	public void updatePossibleAssignments(){
		if(query != null){
			ArrayList<Assignment> possAssign = query.getPossibleAssignments();
			ArrayList<Assignment> neededAssign = query.getAssignments();
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

	/**
	 * Does nothing
	 */
	@Override
	public void feedDialog(IDialogResult input) {
		
	}
	
	/**
	 * 
	 * @return possible methods
	 */
	public Object[] getAvailableMethods(){
		return APerformanceQuery.PMType.values();
	}
	
	/**
	 * Return source names (e.g.for left listViewer) and allocates the local source list
	 * @return source names from the source model
	 */
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
	
	/**
	 * 
	 * @param forWhat is ignored at the moment
	 * @return needed assignments from method
	 */
	public ArrayList<String> getNeededAssignments(String forWhat){
		ArrayList<String> result = new ArrayList<String>();
		for(Assignment a:query.getAssignments()){
			result.add(a.getAttType().toString());
		}
		return result; 
	}
	
	/**
	 * 
	 * @return a list with possible assignments for each needed attribute
	 */
	public ArrayList<ArrayList<String>> getPossibleAssignments(){
		return restructPAssign2; 
	}
	
	/**
	 * Fires general update event that is caught by the view and invokes its update
	 */
	public void updateView(){
		fire(new UpdateEvent(this,UpdateEventType.GeneralUpdate,_cont.getContent()));
	}
	

	
}
