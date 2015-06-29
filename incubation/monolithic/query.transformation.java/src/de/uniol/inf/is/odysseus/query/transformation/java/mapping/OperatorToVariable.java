package de.uniol.inf.is.odysseus.query.transformation.java.mapping;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;


public class OperatorToVariable {
	
	private static Map<ILogicalOperator, String> operatorList  = new HashMap<ILogicalOperator, String>();

	private static int uniqueId = 0;

	public static void addOperator(ILogicalOperator operator){
	
		if(!operatorList.containsKey(operator)){
			operatorList.put(operator, operator.getName().toLowerCase()+getUniqueId());
		}
	
	}
	
	public static String getVariable(ILogicalOperator operator){
		if(operatorList.containsKey(operator)){
			return operatorList.get(operator);
		}else{
			return "";
		}
	}


	synchronized static int getUniqueId()
	{  
		 return uniqueId++;
	   
	}

}
