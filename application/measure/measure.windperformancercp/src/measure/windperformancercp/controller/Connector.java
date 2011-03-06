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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import measure.windperformancercp.ExecutorHandler;
import measure.windperformancercp.event.EventHandler;
import measure.windperformancercp.model.result.PlotSink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryFactory;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;



/**
 * Connects the windperformance measurer with odysseus
 * Something like a facade
 * @author Diana von Gallera
 *
 */
public class Connector extends EventHandler {
	//Odysseus stuff
	IDataDictionary dd;
	User currentUser;
	static Connector instance = new Connector();
	private Map<String,IQuery> queries = new HashMap<String,IQuery>();
	//static List<IQuery> queries = new ArrayList<IQuery>();
	
	private ParameterTransformationConfiguration trafoConfigParam = 
		new ParameterTransformationConfiguration(new TransformationConfiguration("relational", 
				ITimeInterval.class, ILatency.class));
		//new ParameterTransformationConfiguration(new StandardQueryBuildConfiguration());
	ExecutorHandler execHandler;
		

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Connector.class);
		}
		return _logger;
	}
	
	private Connector(){
		try{
		 currentUser = UserManagement.getInstance().getSuperUser();
		 dd = DataDictionaryFactory.getDefaultDataDictionary("WindperformanceRCP");
		 ExecutorHandler.getExecutor().startExecution();
		}
		
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void askExecutor(){
	}
	
	/**
	 * Add query to odysseus
	 * @param query	the query string
	 * @param parserId the id of the parser (CQL, PQL, SCRIPT)
	 * @return
	 */
	public boolean addQuery(String queryText, String parserId, List<String> queryId){
		try{ 
				ArrayList<IQuery> tmp = (ArrayList<IQuery>)ExecutorHandler.getExecutor().addQuery(queryText, parserId, currentUser, dd,
				trafoConfigParam); //new ParameterDefaultRoot(new PlotSink()),
				
				if(queryId != null && queryId.size() == tmp.size()){
					for(int i = 0; i< tmp.size(); i++){
						IQuery query = tmp.get(i);
						queries.put(queryId.get(i), query);
					}
				}
			//	ExecutorHandler.getExecutor().startExecution();
		}
		catch(PlanManagementException pe){
			System.out.println(this.toString()+" - PlanManagementException: "+pe);
			ExecutorHandler.getExecutor().getInfos();
			return false;
		}
		catch(Exception e){
			System.out.println(this.toString()+" - AnotherException: "+e);
			return false;
		}
		return true;
	}
	
	/**
	 * Removes the query to the given int Id (from IQuery)
	 * @param queryID The id from the query returned by the executor
	 * @return if removal was successful
	 */
	public boolean delQuery(int queryID){
		try{
			ExecutorHandler.getExecutor().removeQuery(queryID, currentUser);
			for(String queryKey : queries.keySet()){
				
				if(queries.get(queryKey).getID() == queryID){
					queries.remove(queryKey);
					return true;
				}
			}
			
			//TODO: Error
			System.out.println(this.toString()+" no Query with given Id found "+queryID);
			return false;
		}
		catch(PlanManagementException pe){
			System.out.println(this.toString()+" - PlanManagementException: "+pe);
			ExecutorHandler.getExecutor().getInfos();
			return false;
		}
	
	}
	
	/**
	 * Removes the query to the given String Id (from Performance Measurement)
	 * @param queryID The name of the performance measurement
	 * @return if removal was successful
	 */
	public boolean delQuery(String queryID){
		IQuery query = queries.get(queryID);
		if(query != null){
			try{
				ExecutorHandler.getExecutor().removeQuery(query.getID(), currentUser);
				queries.remove(queryID);
				return true;
			}
			catch(PlanManagementException pe){
			
				System.out.println(this.toString()+" - PlanManagementException: "+pe);
				ExecutorHandler.getExecutor().getInfos();
				return false;
			}
		}
		else {
			//TODO: Error : no query for ID found
			System.out.println(this.toString()+" no Query to given Id "+queryID);
			return false;
		}
	}
	
	
	public static Connector getInstance(){
		return instance;
	}
	
	public Map<String,IQuery> getQueries(){
		return queries;
	}

	
}
