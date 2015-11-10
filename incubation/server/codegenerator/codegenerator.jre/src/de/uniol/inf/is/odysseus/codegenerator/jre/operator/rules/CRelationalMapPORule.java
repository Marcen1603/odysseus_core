package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCRelationalMapPORule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMapPO;

/**
 * This rule generate from a MapAO (inputSchemaType = Tuple) the code for the 
 * RelationalMapPO operator. 
 * 
 * template: operator/relationalMapPO.st
 * 
 * @author MarcPreuschaft
 *
 */
public class CRelationalMapPORule extends  AbstractCRelationalMapPORule<MapAO>{
	
	public CRelationalMapPORule() {
		super(CRelationalMapPORule.class.getName());
	}
	
	@Override
	public CodeFragmentInfo getCode(MapAO logicalOperator) {
		CodeFragmentInfo mapPO = new CodeFragmentInfo();
	
		if(logicalOperator instanceof MapAO){
	
			MapAO mapAO = (MapAO)logicalOperator;
		
			//get unique operator variable
			String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(logicalOperator);
			
			//generate code for inputSchema
			mapPO.addCodeFragmentInfo(CreateJreDefaultCode.getCodeForSDFSchema(mapAO.getInputSchema(), operatorVariable+"Input"));
			 
			//get all needed values for the mapAO
			boolean isAllowNullValue = mapAO.isAllowNullValue();
			boolean isEvaluateOnPunctuation =	mapAO.isEvaluateOnPunctuation();
			boolean isSuppressErrors = mapAO.isSuppressErrors(); 
			
			//generate code for the sdfExpression list, is need for the relationalMapPO
			StringTemplate sdfExpressionListTemplate = new StringTemplate("utils","sdfExpressionList");
			sdfExpressionListTemplate.getSt().add("operatorVariable", operatorVariable);
			sdfExpressionListTemplate.getSt().add("sdfExpressionList", mapAO.getExpressionList());
	
			//render template
			mapPO.addCode(sdfExpressionListTemplate.getSt().render());
			
			//generate code for the relationalMapPO
			StringTemplate relationalMapPOTemplate = new StringTemplate("operator","relationalMapPO");
			relationalMapPOTemplate.getSt().add("operatorVariable", operatorVariable);
			relationalMapPOTemplate.getSt().add("isAllowNullValue", isAllowNullValue);
			relationalMapPOTemplate.getSt().add("isEvaluateOnPunctuation", isEvaluateOnPunctuation);
			relationalMapPOTemplate.getSt().add("isSuppressErrors", isSuppressErrors);
			
			//render template
			mapPO.addCode(relationalMapPOTemplate.getSt().render());
			
			//add framework imports
			mapPO.addFrameworkImport(RelationalMapPO.class.getName());
			mapPO.addFrameworkImport(SDFExpression.class.getName());
		}
		return mapPO;
		
	}


	
}
