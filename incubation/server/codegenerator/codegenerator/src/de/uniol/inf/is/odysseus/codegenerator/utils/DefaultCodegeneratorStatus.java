package de.uniol.inf.is.odysseus.codegenerator.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class DefaultCodegeneratorStatus{

	private static DefaultCodegeneratorStatus instance = null;

	private Map<ILogicalOperator, String> operatorList  = new HashMap<ILogicalOperator, String>();	
	private Map<ILogicalOperator,String> codeReady  = new HashMap<ILogicalOperator, String>();
	private Map<ILogicalOperator, Integer> moreInputs = new HashMap<ILogicalOperator, Integer>();
	private int uniqueId = 0;
	
	
	public static DefaultCodegeneratorStatus getInstance() {
		if (instance == null) {
			instance = new DefaultCodegeneratorStatus();
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
	
	//TODO remove special chars in the operatorName e.g :
	public void addOperator(ILogicalOperator operator){
		
		if(!operatorList.containsKey(operator)){
			String operatorName = operator.getName().toLowerCase();
			operatorName = operatorName.replaceAll("[^a-zA-Z0-9]", "");
			
			operatorList.put(operator, operatorName+getUniqueId());
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


	private synchronized int getUniqueId(){  
		 return uniqueId++;
	}
	
	public  Map<ILogicalOperator, String> getOperatorList(){
		return operatorList;
	}

	public boolean isOperatorReadyforCodegeneration(ILogicalOperator operator){
		if(moreInputs.containsKey(operator)){
			Integer inputAnzahl = moreInputs.get(operator);
			
			if(inputAnzahl == 1){
				return true;
			}else{
				inputAnzahl= inputAnzahl-1;
				return false;
			}
		}
		
		int temp = operator.getSubscribedToSource().size();
		moreInputs.put(operator, temp-1);
		
		return false;
	}
	
}
