package measure.windperformancercp.controller;

import measure.windperformancercp.ExecutorHandler;
import measure.windperformancercp.model.query.IPerformanceQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryFactory;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;



/**
 * Connects the windperformance measurer with odysseus
 * Something like a facade
 * @author blackunicorn
 *
 */
public class Connector {
	//Odysseus stuff
	IDataDictionary dd;
	User currentUser;
	static Connector instance = new Connector();
	
	private ParameterTransformationConfiguration trafoConfigParam = 
		new ParameterTransformationConfiguration(new TransformationConfiguration("relational", ITimeInterval.class));
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
		
		System.out.println(this.toString()+" data dic: "+dd.toString());
		System.out.println(this.toString()+" user: "+currentUser.toString());
		}
		
		catch(Exception e){
			System.out.println(e);
		}
		
	}
	
	public void askExecutor(){
		
	}
	
	public void addQuery(IPerformanceQuery query){
	}
	
	public void addQuery(String query, String parserId){
		try{
			ExecutorHandler.getExecutor().addQuery(query, parserId, currentUser, dd, trafoConfigParam);
		}
		catch(PlanManagementException pe){
			System.out.println(this.toString()+" - PlanManagementException: "+pe);
			ExecutorHandler.getExecutor().getInfos();
		}
		
	}
	
	public void delQuery(int queryID){
		try{
			ExecutorHandler.getExecutor().removeQuery(queryID, currentUser);
		}
		catch(PlanManagementException pe){
			System.out.println(this.toString()+" - PlanManagementException: "+pe);
			ExecutorHandler.getExecutor().getInfos();
		}
	
	}
	
	public static Connector getInstance(){
		return instance;
	}
	
}
