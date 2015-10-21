package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.ICOperatorRule;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.ICOperatorRuleComparator;


public class COperatorRuleRegistry {
	
	private static Logger LOG = LoggerFactory.getLogger(COperatorRuleRegistry.class);
	
	static Map<String, Map<String,ICOperatorRule<ILogicalOperator>>> operatorRuleList = new HashMap<String, Map<String,ICOperatorRule<ILogicalOperator>>>();
	
	
	public static ICOperatorRule<ILogicalOperator> getOperatorRules(String targetPlatform, ILogicalOperator operator, TransformationConfiguration transformationConfiguration){
		List<ICOperatorRule<ILogicalOperator>> acceptedRules = new ArrayList<ICOperatorRule<ILogicalOperator>>();
		
		
		//targetplatform vorhanden
		if(operatorRuleList.containsKey(targetPlatform.toLowerCase())){
			
			Map<String,ICOperatorRule<ILogicalOperator>> rules = operatorRuleList.get(targetPlatform.toLowerCase());
			
			for (Entry<String, ICOperatorRule<ILogicalOperator>> entry : rules.entrySet())
			{
				Class<ILogicalOperator> conditionClass = entry.getValue().getConditionClass();
				//Class<ILogicalOperator> conditionClass = null;
				if(conditionClass.equals(operator.getClass())){
					if(entry.getValue().isExecutable(conditionClass.cast(operator), transformationConfiguration )){
						acceptedRules.add(entry.getValue());
					}
				}
					
			}
			
		}
	
		Collections.sort(acceptedRules, new ICOperatorRuleComparator());
		
		if(acceptedRules.size() == 0){
			return null;
		}else{
			return acceptedRules.get(0); 
		}
	
	}
	

					   
	public static void registerOperatorRule(ICOperatorRule<ILogicalOperator> rule){
	
		//Programmiersprache noch nicht vorhanden
		if(!operatorRuleList.containsKey(rule.getTargetPlatform().toLowerCase())){
		
			//Operator noch nicht vorhanden?
			if(operatorRuleList.get(rule.getTargetPlatform().toLowerCase())== null || !operatorRuleList.get(rule.getTargetPlatform().toLowerCase()).containsKey(rule.getName().toLowerCase())){
				//Operatorname + ITransformationOperator Map erzeugen
				Map<String,ICOperatorRule<ILogicalOperator>> tempMap = new HashMap<String,ICOperatorRule<ILogicalOperator>>();
				tempMap.put(rule.getName(), rule);
				
				//verschachtelte Map abspeichern
				operatorRuleList.put(rule.getTargetPlatform().toLowerCase(), tempMap);
			}else{
				LOG.debug("Operator "+rule.getName()+" -> "+rule.getTargetPlatform().toLowerCase()+" already added" );
			}
			
		}else{
			//Programmiersprache existiert bereits nur Operator hinzufügen			
			if(!operatorRuleList.get(rule.getTargetPlatform().toLowerCase()).containsKey(rule.getName())){
				Map<String,ICOperatorRule<ILogicalOperator>> tempMap = operatorRuleList.get(rule.getTargetPlatform().toLowerCase());
				tempMap.put(rule.getName(), rule);
			}else{
				LOG.debug("Operator "+rule.getName()+" -> "+rule.getTargetPlatform().toLowerCase()+" already added" );
			}
		}
		
	}
	
	public static void unregisterOperatorRule(ICOperatorRule<ILogicalOperator> rule){
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
