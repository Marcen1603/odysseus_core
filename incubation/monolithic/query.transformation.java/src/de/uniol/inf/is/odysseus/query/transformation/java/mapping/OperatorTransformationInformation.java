package de.uniol.inf.is.odysseus.query.transformation.java.mapping;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class OperatorTransformationInformation {

	private static OperatorTransformationInformation instance = null;

	private Map<ILogicalOperator, String> operatorList  = new HashMap<ILogicalOperator, String>();
	
	private int uniqueId = 0;
	
	public static OperatorTransformationInformation getInstance() {
		if (instance == null) {
			instance = new OperatorTransformationInformation();
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
