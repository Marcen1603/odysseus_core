package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCRelationalMapPORule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMapPO;

public class CRelationalMapPORule extends  AbstractCRelationalMapPORule<MapAO>{
	
	public CRelationalMapPORule() {
		super(CRelationalMapPORule.class.getName());
	}
	
	@Override
	public CodeFragmentInfo getCode(MapAO logicalOperator) {
		CodeFragmentInfo mapPO = new CodeFragmentInfo();
	
		if(logicalOperator instanceof MapAO){
		
	
			MapAO mapAO = (MapAO)logicalOperator;
		
			String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(logicalOperator);
			
			mapPO.addCodeFragmentInfo(CreateJreDefaultCode.getCodeForSDFSchema( mapAO.getInputSchema(), operatorVariable+"Input"));
			 
			boolean isAllowNullValue = mapAO.isAllowNullValue();
			boolean isEvaluateOnPunctuation =	mapAO.isEvaluateOnPunctuation();
			boolean isSuppressErrors = mapAO.isSuppressErrors(); 
			
		
			StringTemplate sdfExpressionListTemplate = new StringTemplate("utils","sdfExpressionList");
			sdfExpressionListTemplate.getSt().add("operatorVariable", operatorVariable);
			sdfExpressionListTemplate.getSt().add("sdfExpressionList", mapAO.getExpressionList());
	
			mapPO.addCode(sdfExpressionListTemplate.getSt().render());
			
			StringTemplate relationalMapPOTemplate = new StringTemplate("operator","relationalMapPO");
			relationalMapPOTemplate.getSt().add("operatorVariable", operatorVariable);
			relationalMapPOTemplate.getSt().add("isAllowNullValue", isAllowNullValue);
			relationalMapPOTemplate.getSt().add("isEvaluateOnPunctuation", isEvaluateOnPunctuation);
			relationalMapPOTemplate.getSt().add("isSuppressErrors", isSuppressErrors);
			
			
			mapPO.addCode(relationalMapPOTemplate.getSt().render());
			
			
			mapPO.addFrameworkImport(RelationalMapPO.class.getName());
			mapPO.addFrameworkImport(SDFExpression.class.getName());
		}
		return mapPO;
		
	}


	
}
