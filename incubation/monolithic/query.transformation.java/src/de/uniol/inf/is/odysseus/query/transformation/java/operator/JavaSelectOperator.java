package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.parser.pql.relational.RelationalPredicateBuilder;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;


public class JavaSelectOperator extends AbstractTransformationOperator{
	
	public JavaSelectOperator(){
		super(SelectPO.class, new SelectAO().getClass().getSimpleName(),"Java");
	}
	
	
/*
 * 
		SelectPO<?> selectPO = new SelectPO(selectAO.getPredicate());
        selectPO.setHeartbeatRate(selectAO.getHeartbeatRate());
 */
	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		CodeFragmentInfo selectPO = new CodeFragmentInfo();
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		
		SelectAO selectAO = (SelectAO) operator;
		IPredicate<?> predicate = selectAO.getPredicate();
	
		String predicateValue = predicate.toString();
		IExpression<?> mepExpression  = MEP.getInstance().parse(predicateValue);
		
		TransformationInformation.getInstance().addMEPFunction(mepExpression);
		
		StringTemplate selectTemplate = new StringTemplate("selectPO");
		selectTemplate.getSt().add("operatorVariable", operatorVariable);
		selectTemplate.getSt().add("predicateValue", predicateValue);
		

		selectPO.addCode(selectTemplate.getSt().render());
		
		selectPO.addImport(DirectAttributeResolver.class.getName());
		selectPO.addImport(RelationalPredicateBuilder.class.getName());
		selectPO.addImport(RelationalPredicate.class.getName());
		
		return selectPO;
	}


	@Override
	public void defineImports() {
		// TODO Auto-generated method stub
		
	}

}
