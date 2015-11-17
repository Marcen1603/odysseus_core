package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.model.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCHashFragmentAORule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.physicaloperator.HashFragmentPO;

/**
 * This rule generate from a HashFragmentAO the code for the 
 * HashFragmentPO operator. 
 * 
 * template: operator/hashFragmentPO.st
 * 
 * @author MarcPreuschaft
 *
 */
public class CHashFragmentPORule extends AbstractCHashFragmentAORule<HashFragmentAO>{

	public CHashFragmentPORule() {
		super(CHashFragmentPORule.class.getName());
	}


	@Override
	public CodeFragmentInfo getCode(HashFragmentAO operator) {
		CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();
		
		//get unique operator variable
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);
	
		//generate code for the operator attributes (Fragment)
		codeFragmentInfo.addCodeFragmentInfo(CreateJreDefaultCode.getCodeForSDFAttributeList(operator.getAttributes(), operatorVariable+"Fragment"));
		
		//generate code for the inputSchema
		codeFragmentInfo.addCodeFragmentInfo(CreateJreDefaultCode.getCodeForSDFSchema(operator.getInputSchema(), operatorVariable+"Input"));

		//generate code for the hashFragmentPO
		StringTemplate hashFragmentTemplate = new StringTemplate("operator","hashFragmentPO");
		hashFragmentTemplate.getSt().add("operatorVariable", operatorVariable);
		hashFragmentTemplate.getSt().add("optimizeDistribution", operator.isOptimizeDistribution());
		hashFragmentTemplate.getSt().add("numFragments", operator.getNumberOfFragments());
		hashFragmentTemplate.getSt().add("heartbeatRate", operator.getHeartbeatrate());
		
		//render template
		codeFragmentInfo.addCode(hashFragmentTemplate.getSt().render());
		
		//add framework imports
		codeFragmentInfo.addFrameworkImport(HashFragmentPO.class.getName());
		
		return codeFragmentInfo;
	}

}