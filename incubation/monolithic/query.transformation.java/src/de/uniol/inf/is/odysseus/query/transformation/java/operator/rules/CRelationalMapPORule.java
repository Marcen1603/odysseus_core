package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMapPO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.JavaTransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractCRelationalMapPORule;

public class CRelationalMapPORule extends  AbstractCRelationalMapPORule{
	
	public CRelationalMapPORule() {
		super(CRelationalMapPORule.class.getName(), "java");
	}
	
	@Override
	public CodeFragmentInfo getCode(ILogicalOperator logicalOperator) {
		
		CodeFragmentInfo mapPO = new CodeFragmentInfo();
		
		if(logicalOperator instanceof MapAO){
		
			MapAO mapAO = (MapAO)logicalOperator;
			String operatorVariable = JavaTransformationInformation.getInstance().getVariable(logicalOperator);
	
			SDFExpression[] sdfExpression = mapAO.getExpressionList().toArray(new SDFExpression[0]);
			
			
			for (SDFExpression expression : sdfExpression) {
				
			    System.out.println(expression.getExpressionString());
			    SDFExpression test = new SDFExpression(expression.getExpressionString(), MEP.getInstance());
			    System.out.println(test.getClass().getSimpleName());
			}
			
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
