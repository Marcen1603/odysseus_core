package de.uniol.inf.is.odysseus.query.transformation.java.mapping;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;


public class OperatorToVariable {
	
	private static Map<ILogicalOperator, Integer> operatorList  = new HashMap<ILogicalOperator, Integer>();

	private static int uniqueId = 0;


	
	public static void addOperator(ILogicalOperator operator){
	
		if(!operatorList.containsKey(operator)){
			operatorList.put(operator, getUniqueId());
		}
	
	}
	
	
	public static int getVariable(ILogicalOperator operator){
		if(operatorList.containsKey(operator)){
			return operatorList.get(operator);
		}else{
			return 0;
		}
	}
	

	
	synchronized static int getUniqueId()
	{  
		 return uniqueId++;
	   
	}

}
