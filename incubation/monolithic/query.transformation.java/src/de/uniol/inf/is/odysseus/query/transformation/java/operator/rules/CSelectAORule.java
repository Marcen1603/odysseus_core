package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.parser.pql.relational.RelationalPredicateBuilder;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.OperatorTransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractCSelectAORule;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class CSelectAORule extends AbstractCSelectAORule{

	public CSelectAORule() {
		super(CSelectAORule.class.getName(), "Java");
	}


	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
	CodeFragmentInfo selectPO = new CodeFragmentInfo();
		
		String operatorVariable = OperatorTransformationInformation.getInstance().getVariable(operator);
		
		SelectAO selectAO = (SelectAO) operator;
		IPredicate<?> predicate = selectAO.getPredicate();
	
		String predicateValue = predicate.toString();

	
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
