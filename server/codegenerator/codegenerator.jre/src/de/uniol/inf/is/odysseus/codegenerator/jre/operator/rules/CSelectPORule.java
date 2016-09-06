package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.model.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCSelectAORule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.parser.pql.relational.RelationalPredicateBuilder;

/**
 * This rule generate from a SelectAO (inputSchemaType = Tuple) the code for the 
 * SelectPO operator. 
 * 
 * template: operator/selectPO.st
 * 
 * @author MarcPreuschaft
 *
 */
public class CSelectPORule extends AbstractCSelectAORule<SelectAO>{

	public CSelectPORule() {
		super(CSelectPORule.class.getName());
	}


	@Override
	public CodeFragmentInfo getCode(SelectAO operator) {
		CodeFragmentInfo selectPO = new CodeFragmentInfo();
		
		//get unique operator variable
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);
		
		SelectAO selectAO = (SelectAO) operator;
		IPredicate<?> predicate = selectAO.getPredicate();
	
		//get predicate from the selectAO
		String predicateValue = predicate.toString();

		//generate code for selectPO
		StringTemplate selectTemplate = new StringTemplate("operator","selectPO");
		selectTemplate.getSt().add("operatorVariable", operatorVariable);
		selectTemplate.getSt().add("predicateValue", predicateValue);
		selectPO.addCode(selectTemplate.getSt().render());
		
		//add framework imports
		selectPO.addFrameworkImport(DirectAttributeResolver.class.getName());
		selectPO.addFrameworkImport(RelationalPredicateBuilder.class.getName());
		selectPO.addFrameworkImport(RelationalExpression.class.getName());
		selectPO.addFrameworkImport(SelectPO.class.getName());
		
		return selectPO;
	}

}
