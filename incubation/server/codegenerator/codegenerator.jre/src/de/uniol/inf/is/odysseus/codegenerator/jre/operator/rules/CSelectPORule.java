package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCSelectAORule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.parser.pql.relational.RelationalPredicateBuilder;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class CSelectPORule extends AbstractCSelectAORule<SelectAO>{

	public CSelectPORule() {
		super(CSelectPORule.class.getName());
	}


	@Override
	public CodeFragmentInfo getCode(SelectAO operator) {
		CodeFragmentInfo selectPO = new CodeFragmentInfo();
		
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);
		
		SelectAO selectAO = (SelectAO) operator;
		IPredicate<?> predicate = selectAO.getPredicate();
	
		String predicateValue = predicate.toString();

	
		StringTemplate selectTemplate = new StringTemplate("operator","selectPO");
		selectTemplate.getSt().add("operatorVariable", operatorVariable);
		selectTemplate.getSt().add("predicateValue", predicateValue);
		selectPO.addCode(selectTemplate.getSt().render());
		
		selectPO.addFrameworkImport(DirectAttributeResolver.class.getName());
		selectPO.addFrameworkImport(RelationalPredicateBuilder.class.getName());
		selectPO.addFrameworkImport(RelationalPredicate.class.getName());
		selectPO.addFrameworkImport(SelectPO.class.getName());
		
		
		return selectPO;
	}

}
