package de.uniol.inf.is.odysseus.query.codegenerator.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class JavaTransformationInformation{

	private static JavaTransformationInformation instance = null;

	private Map<ILogicalOperator, String> operatorList  = new HashMap<ILogicalOperator, String>();
	
	private Map<ILogicalOperator,String> codeReady  = new HashMap<ILogicalOperator, String>();
	
	private int uniqueId = 0;
	
	public static JavaTransformationInformation getInstance() {
		if (instance == null) {
			instance = new JavaTransformationInformation();
		}
		return instance;
	}
	
	public static void clear(){
		instance = null;
	}

	public boolean isOperatorAdded(ILogicalOperator operator){
		if(operatorList.containsKey(operator)){
			return true;
		}else{
			return false;
		}
	}
	
	public void addOperator(ILogicalOperator operator){
		
		if(!operatorList.containsKey(operator)){
			operatorList.put(operator, operator.getName().toLowerCase()+getUniqueId());
		}
		
	}
	
	
	public boolean allOperatorExistForSubscriptions(List<ILogicalOperator> operatorList){
		if(operatorList.isEmpty()){
			return false;
		}
		
		for(ILogicalOperator op : operatorList){
			if(!codeReady.containsKey(op)){
				return false;
			}
		
		}
		
		return true;
	}
	
	public boolean isOperatorCodeReady(ILogicalOperator operator){
		if(codeReady.containsKey(operator)){
			return true;
		}else{
			return false;
		}
	}
	
	public void addOperatorToCodeReady(ILogicalOperator operator){

		if(!codeReady.containsKey(operator)){
			codeReady.put(operator, "0");
		}
	
	}
	
	public void setOperatorList(Map<ILogicalOperator, String> operatorList){
		this.operatorList = operatorList;
	}
	
	public  String getVariable(ILogicalOperator operator){
		if(operatorList.containsKey(operator)){
			return operatorList.get(operator);
		}else{
			return "";
		}
	}


	private synchronized int getUniqueId()
	{  
		 return uniqueId++;
	   
	}
	
	public  Map<ILogicalOperator, String> getOperatorList(){
		return operatorList;
	}

}
