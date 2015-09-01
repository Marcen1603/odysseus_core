package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.parser.pql.relational.RelationalPredicateBuilder;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCSelectAORule;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.JavaTransformationInformation;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class CSelectAORule extends AbstractCSelectAORule<SelectAO>{

	public CSelectAORule() {
		super(CSelectAORule.class.getName());
	}


	@Override
	public CodeFragmentInfo getCode(SelectAO operator) {
	CodeFragmentInfo selectPO = new CodeFragmentInfo();
		
		String operatorVariable = JavaTransformationInformation.getInstance().getVariable(operator);
		
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