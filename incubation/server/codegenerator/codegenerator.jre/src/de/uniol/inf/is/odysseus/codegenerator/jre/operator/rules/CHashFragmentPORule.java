package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCHashFragmentAORule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.physicaloperator.HashFragmentPO;

public class CHashFragmentPORule extends AbstractCHashFragmentAORule<HashFragmentAO>{

	public CHashFragmentPORule() {
		super(CHashFragmentPORule.class.getName());
	}


	@Override
	public CodeFragmentInfo getCode(HashFragmentAO operator) {
		CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();
		
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);
	
		
		codeFragmentInfo.addCodeFragmentInfo(CreateJreDefaultCode.getCodeForSDFAttributeList(operator.getAttributes(), operatorVariable+"Fragment"));
		codeFragmentInfo.addCodeFragmentInfo(CreateJreDefaultCode.getCodeForSDFSchema(operator.getInputSchema(), operatorVariable+"Input"));

		
		StringTemplate hashFragmentTemplate = new StringTemplate("operator","hashFragmentPO");
		hashFragmentTemplate.getSt().add("operatorVariable", operatorVariable);
		hashFragmentTemplate.getSt().add("optimizeDistribution", operator.isOptimizeDistribution());
		hashFragmentTemplate.getSt().add("numFragments", operator.getNumberOfFragments());
		hashFragmentTemplate.getSt().add("heartbeatRate", operator.getHeartbeatrate());
		
		
		codeFragmentInfo.addCode(hashFragmentTemplate.getSt().render());
		codeFragmentInfo.addFrameworkImport(HashFragmentPO.class.getName());
		
		return codeFragmentInfo;
	}

}