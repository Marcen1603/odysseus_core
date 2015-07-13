package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;


public class JavaSelectOperator extends AbstractTransformationOperator{
	
	public JavaSelectOperator(){
		this.implClass = SelectPO.class;
		this.name =  new SelectAO().getClass().getSimpleName();
		this.targetPlatform = "Java";
		defaultImports();
	}
	
	
/*
 * 
		SelectPO<?> selectPO = new SelectPO(selectAO.getPredicate());
        selectPO.setHeartbeatRate(selectAO.getHeartbeatRate());
 */
	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		CodeFragmentInfo selectPO = new CodeFragmentInfo();
		
		StringBuilder code = new StringBuilder();
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		
		SelectAO selectAO = (SelectAO) operator;
		IPredicate<?> predicate = selectAO.getPredicate();
	
		String predicateValue = predicate.toString();
		IExpression<?> mepExpression  = MEP.getInstance().parse(predicateValue);
		TransformationInformation.getInstance().addMEPFunction(mepExpression);
		
		code.append("\n");
		code.append("\n");
		code.append("DirectAttributeResolver " +operatorVariable+"DirectAttriuteResolver = new DirectAttributeResolver("+operatorVariable+"SDFSchema);");
		code.append("\n");
		code.append("RelationalPredicateBuilder "+ operatorVariable+"RelationalPredicateBuilder = new  RelationalPredicateBuilder();");
		code.append("\n");
		code.append("RelationalPredicate "+operatorVariable+"Predicate = (RelationalPredicate)"+operatorVariable+"RelationalPredicateBuilder.createPredicate("+operatorVariable+"DirectAttriuteResolver, \""+predicateValue+"\");");
		code.append("\n");
		
		code.append(operatorVariable+"Predicate.init("+operatorVariable+"SDFSchema, null);");
		code.append("\n");
		
		code.append("SelectPO "+operatorVariable+"PO = new SelectPO("+operatorVariable+"Predicate);");
		code.append("\n");
		code.append("\n");
		
		
		
		selectPO.addCode(code.toString());
		
		return selectPO;
	}


	@Override
	public void defineImports() {
		// TODO Auto-generated method stub
		
	}

}
