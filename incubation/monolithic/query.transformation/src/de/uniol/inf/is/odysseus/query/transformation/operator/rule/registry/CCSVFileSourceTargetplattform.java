package de.uniol.inf.is.odysseus.query.transformation.operator.rule.registry;

import java.util.List;

import de.uniol.inf.is.odysseus.query.transformation.operator.rule.IOperatorRule;


public class CCSVFileSourceTargetplattform implements IOperatorRuleInitializer {
	List<String> targetPlattformList;
	
	private IOperatorRule basedRule;
	
	public CCSVFileSourceTargetplattform(){
		

		initTargetplattforms();
	}
	
	@Override
	public String getName() {
		return "CCSVFileSourceRule";
	}

	@Override
	public List<String> getTargetplattform() {
		return targetPlattformList;
	}
	
	private void initTargetplattforms(){
		targetPlattformList.add("Java");
		targetPlattformList.add("Dalvik");
	}

}
