package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IPredicateBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.parser.pql.relational.RelationalPredicateBuilder;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.OperatorToVariable;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.TransformSDFSchema;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;


public class JavaSelectOperator extends AbstractTransformationOperator{
	
  private final String name =  new SelectAO().getClass().getSimpleName();
  private final String targetPlatform = "Java";
  
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
		IPredicate predicate = selectAO.getPredicate();
		
		String predicateValue = predicate.toString();

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
		Set<String> importList = new HashSet<String>();
		importList.add("de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver");
		importList.add("de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO");
		importList.add("de.uniol.inf.is.odysseus.parser.pql.relational.RelationalPredicateBuilder");
		importList.add("de.uniol.inf.is.odysseus.core.predicate.IPredicate");
		
		

		return importList;
		
		
	
	}


	
	
	
}
