package de.uniol.inf.is.odysseus.query.transformation.operator.rule.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.IOperatorRule;


public class OperatorRuleRegistry {
	
	private static Logger LOG = LoggerFactory.getLogger(OperatorRuleRegistry.class);
	
	static Map<String, Map<String,IOperatorRule>> operatorRuleList = new HashMap<String, Map<String,IOperatorRule>>();
	
	
	public static void getOperatorRules(String targetPlatform, ILogicalOperator operator, TransformationConfiguration transformationConfiguration){
		List<IOperatorRule> acceptedRules = new ArrayList<IOperatorRule>();
		
		//targetplatform vorhanden
		if(operatorRuleList.containsKey(targetPlatform.toLowerCase())){
			
			
			Map<String,IOperatorRule> rules = operatorRuleList.get(targetPlatform.toLowerCase());
			
			for (Entry<String, IOperatorRule> entry : rules.entrySet())
			{
				if(entry.getValue().isExecutable(operator, transformationConfiguration )){
					
					acceptedRules.add(entry.getValue());
				}
			}
			
		}
		 System.out.println(acceptedRules);
	}
				
					   
	public static void registerOperatorRule(IOperatorRule rule){
	
		//Programmiersprache noch nicht vorhanden
		if(!operatorRuleList.containsKey(rule.getTargetPlatform().toLowerCase())){
		
			//Operator noch nicht vorhanden?
			if(operatorRuleList.get(rule.getTargetPlatform().toLowerCase())== null || !operatorRuleList.get(rule.getTargetPlatform().toLowerCase()).containsKey(rule.getName().toLowerCase())){
				//Operatorname + ITransformationOperator Map erzeugen
				Map<String,IOperatorRule> tempMap = new HashMap<String,IOperatorRule>();
				tempMap.put(rule.getName().toLowerCase(), rule);
				
				//verschachtelte Map abspeichern
				operatorRuleList.put(rule.getTargetPlatform().toLowerCase(), tempMap);
			}else{
				LOG.debug("Operator "+rule.getName()+" -> "+rule.getTargetPlatform().toLowerCase()+" already added" );
			}
			
		}else{
			//Programmiersprache existiert bereits nur Operator hinzufügen			
			if(!operatorRuleList.get(rule.getTargetPlatform().toLowerCase()).containsKey(rule.getName())){
				Map<String,IOperatorRule> tempMap = operatorRuleList.get(rule.getTargetPlatform().toLowerCase());
				tempMap.put(rule.getName().toLowerCase(), rule);
			}else{
				LOG.debug("Operator "+rule.getName()+" -> "+rule.getTargetPlatform().toLowerCase()+" already added" );
			}
		}
		
	}
	
	public static void unregisterOperatorRule(IOperatorRule rule){
		if(operatorRuleList.containsKey(rule.getTargetPlatform().toLowerCase())){
			if(operatorRuleList.get(rule.getTargetPlatform().toLowerCase()).containsKey(rule.getName())){
				operatorRuleList.get(rule.getTargetPlatform().toLowerCase()).remove(rule.getName());
			}
			
			if(operatorRuleList.get(rule.getTargetPlatform().toLowerCase()).isEmpty()){
				operatorRuleList.remove(rule.getTargetPlatform().toLowerCase());
			}
		}
	}
	
}
