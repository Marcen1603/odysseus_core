package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.NeededMEPFunctions;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.OperatorToVariable;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.TransformSDFSchema;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;


public class JavaSelectOperator extends AbstractTransformationOperator{
	
  private final String name =  new SelectAO().getClass().getSimpleName();
  private final String targetPlatform = "Java";
  
  private Set<String> importList = new HashSet<String>();
  
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTargetPlatform() {
		return targetPlatform;
	}

	
/*
 * 
		SelectPO<?> selectPO = new SelectPO(selectAO.getPredicate());
        selectPO.setHeartbeatRate(selectAO.getHeartbeatRate());
 */
	@Override
	public String getCode(ILogicalOperator operator) {
		StringBuilder code = new StringBuilder();
		String operatorVariable = OperatorToVariable.getVariable(operator);
		
		SelectAO selectAO = (SelectAO) operator;
		IPredicate<?> predicate = selectAO.getPredicate();
		
	
		
		String predicateValue = predicate.toString();
		
		
		IExpression<?> test =MEP.getInstance().parse(predicateValue);
		NeededMEPFunctions.addMEPFunction(test.toFunction().getClass().getName(), test.toFunction().getClass().getSimpleName());
	
		code.append("\n");
		code.append("\n");
		code.append(TransformSDFSchema.getCodeForSDFSchema(selectAO.getOutputSchema(), operatorVariable));
		code.append("\n");
		code.append("DirectAttributeResolver directAttriuteResolver = new DirectAttributeResolver("+operatorVariable+"SDFSchema);");
		code.append("\n");
		code.append("RelationalPredicateBuilder predicateNeu = new  RelationalPredicateBuilder();");
		code.append("\n");
		code.append("IPredicate "+operatorVariable+"Predicate = predicateNeu.createPredicate(directAttriuteResolver, \""+predicateValue+"\");");
		
		
		code.append("\n");
		
		code.append("SelectPO "+operatorVariable+"PO = new SelectPO("+operatorVariable+"Predicate);");
		code.append("\n");
		code.append("\n");
		return code.toString();
	}



	@Override
	public Set<String> getNeededImports() {
		importList.add(SelectPO.class.getPackage().getName()+"."+SelectPO.class.getSimpleName());
		return importList;
	}


	
	
	
}
