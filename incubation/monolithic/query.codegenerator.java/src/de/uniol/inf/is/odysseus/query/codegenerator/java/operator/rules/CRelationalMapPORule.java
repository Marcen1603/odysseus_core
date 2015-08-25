package de.uniol.inf.is.odysseus.query.codegenerator.java.operator.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMapPO;
import de.uniol.inf.is.odysseus.query.codegenerator.java.mapping.JavaTransformationInformation;
import de.uniol.inf.is.odysseus.query.codegenerator.java.utils.CreateJavaDefaultCode;
import de.uniol.inf.is.odysseus.query.codegenerator.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCRelationalMapPORule;

public class CRelationalMapPORule extends  AbstractCRelationalMapPORule{
	
	public CRelationalMapPORule() {
		super(CRelationalMapPORule.class.getName());
	}
	
	@Override
	public CodeFragmentInfo getCode(ILogicalOperator logicalOperator) {
		CodeFragmentInfo mapPO = new CodeFragmentInfo();
	
		if(logicalOperator instanceof MapAO){
		
	
			MapAO mapAO = (MapAO)logicalOperator;
		
			String operatorVariable = JavaTransformationInformation.getInstance().getVariable(logicalOperator);
			
			mapPO.addCodeFragmentInfo(CreateJavaDefaultCode.getCodeForSDFSchema( mapAO.getInputSchema(), operatorVariable+"Input"));
			 
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
			
			
			mapPO.addImport(RelationalMapPO.class.getName());
			mapPO.addImport(SDFExpression.class.getName());
		}
		return mapPO;
		
	}


	
}
