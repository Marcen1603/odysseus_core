package de.uniol.inf.is.odysseus.codegenerator.operator.rule.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.codegenerator.operator.rule.ICOperatorRule;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.ICOperatorRuleComparator;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

/**
 * Registry for operator rules for the codegenerator 
 * 
 * @author MarcPreuschaft
 *
 */
public class COperatorRuleRegistry {
	
	private static Logger LOG = LoggerFactory.getLogger(COperatorRuleRegistry.class);
	
	//operator rules for codegeneration
	static Map<String, Map<String,ICOperatorRule<ILogicalOperator>>> operatorRuleList = new HashMap<String, Map<String,ICOperatorRule<ILogicalOperator>>>();
	
	/**
	 * return a applicable operator rule 
	 *
	 * @param targetPlatform
	 * @param operator
	 * @param transformationConfiguration
	 * @return
	 */
	public static ICOperatorRule<ILogicalOperator> getOperatorRules(String targetPlatform, ILogicalOperator operator, TransformationConfiguration transformationConfiguration){
		List<ICOperatorRule<ILogicalOperator>> acceptedRules = new ArrayList<ICOperatorRule<ILogicalOperator>>();
		
		//targetplatform exist?
		if(operatorRuleList.containsKey(targetPlatform.toLowerCase())){
			
			//get all rules for the given targetPlatform
			Map<String,ICOperatorRule<ILogicalOperator>> rules = operatorRuleList.get(targetPlatform.toLowerCase());
			
			for (Entry<String, ICOperatorRule<ILogicalOperator>> entry : rules.entrySet())
			{
				Class<ILogicalOperator> conditionClass = entry.getValue().getConditionClass();
				//check first the condition class
				if(conditionClass.equals(operator.getClass())){
					//check the rule with the executable function
					if(entry.getValue().isExecutable(conditionClass.cast(operator), transformationConfiguration )){
						//rule fit then add this to the acceptedRules list
						acceptedRules.add(entry.getValue());
					}
				}
					
			}
			
		}
		
		//sort all executable rules
		Collections.sort(acceptedRules, new ICOperatorRuleComparator());
		
		if(acceptedRules.size() == 0){
			return null;
		}else{
			//return the rule with the highest priority
			return acceptedRules.get(0); 
		}
	
	}
	

	/**
	 * registers an new operatorRule (OSGI method)
	 * 				   
	 * @param rule
	 */
	public static void registerOperatorRule(ICOperatorRule<ILogicalOperator> rule){
		//check if the targetPlatform exist
		if(!operatorRuleList.containsKey(rule.getTargetPlatform().toLowerCase())){
		
			//check if the rule exist
			if(operatorRuleList.get(rule.getTargetPlatform().toLowerCase())== null || !operatorRuleList.get(rule.getTargetPlatform().toLowerCase()).containsKey(rule.getName().toLowerCase())){
				//add operator rule with name 
				Map<String,ICOperatorRule<ILogicalOperator>> tempMap = new HashMap<String,ICOperatorRule<ILogicalOperator>>();
				tempMap.put(rule.getName(), rule);
				
				//save the operator rule map based on the targetPlatform name
				operatorRuleList.put(rule.getTargetPlatform().toLowerCase(), tempMap);
			}else{
				LOG.debug("Operator "+rule.getName()+" -> "+rule.getTargetPlatform().toLowerCase()+" already added" );
			}
			
		}else{
			//targetPlaftrom already exist, add only the operator rule		
			if(!operatorRuleList.get(rule.getTargetPlatform().toLowerCase()).containsKey(rule.getName())){
				Map<String,ICOperatorRule<ILogicalOperator>> tempMap = operatorRuleList.get(rule.getTargetPlatform().toLowerCase());
				tempMap.put(rule.getName(), rule);
			}else{
				LOG.debug("Operator "+rule.getName()+" -> "+rule.getTargetPlatform().toLowerCase()+" already added" );
			}
		}
		
	}
	
	/**
	 * unregisters operatorRule (OSGI method)
	 * 
	 * @param rule
	 */
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
