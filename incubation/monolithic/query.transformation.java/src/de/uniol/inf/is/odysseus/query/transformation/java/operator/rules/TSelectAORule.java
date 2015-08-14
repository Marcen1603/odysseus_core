package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.parser.pql.relational.RelationalPredicateBuilder;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractRule;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class TSelectAORule extends AbstractRule{

	public TSelectAORule() {
		super("TSelectAORule", "Java");
	}

	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		if(logicalOperator instanceof SelectAO){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public Class<?> getConditionClass() {
		return SelectAO.class;
	}

	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
	CodeFragmentInfo selectPO = new CodeFragmentInfo();
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		
		SelectAO selectAO = (SelectAO) operator;
		IPredicate<?> predicate = selectAO.getPredicate();
	
		String predicateValue = predicate.toString();
		IExpression<?> mepExpression  = MEP.getInstance().parse(predicateValue);
		
		TransformationInformation.getInstance().addMEPFunction(mepExpression);
		
		StringTemplate selectTemplate = new StringTemplate("operator","selectPO");
		selectTemplate.getSt().add("operatorVariable", operatorVariable);
		selectTemplate.getSt().add("predicateValue", predicateValue);
		

		selectPO.addCode(selectTemplate.getSt().render());
		
		selectPO.addImport(DirectAttributeResolver.class.getName());
		selectPO.addImport(RelationalPredicateBuilder.class.getName());
		selectPO.addImport(RelationalPredicate.class.getName());
		selectPO.addImport(SelectPO.class.getName());
		
		return selectPO;
	}

}
