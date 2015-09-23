package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.JreCodegeneratorStatus;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCHashFragmentAORule;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.physicaloperator.HashFragmentPO;

public class CHashFragmentAORule extends AbstractCHashFragmentAORule<HashFragmentAO>{

	public CHashFragmentAORule() {
		super(CHashFragmentAORule.class.getName());
	}


	@Override
	public CodeFragmentInfo getCode(HashFragmentAO operator) {
		CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();
		
		String operatorVariable = JreCodegeneratorStatus.getInstance().getVariable(operator);
	
		
		codeFragmentInfo.addCodeFragmentInfo(CreateJreDefaultCode.getCodeForSDFAttributeList(operator.getAttributes(), operatorVariable+"Fragment"));
		codeFragmentInfo.addCodeFragmentInfo(CreateJreDefaultCode.getCodeForSDFSchema(operator.getInputSchema(), operatorVariable+"Input"));

		
		StringTemplate hashFragmentTemplate = new StringTemplate("operator","hashFragmentPO");
		hashFragmentTemplate.getSt().add("operatorVariable", operatorVariable);
		hashFragmentTemplate.getSt().add("optimizeDistribution", operator.isOptimizeDistribution());
		hashFragmentTemplate.getSt().add("numFragments", operator.getNumberOfFragments());
		hashFragmentTemplate.getSt().add("heartbeatRate", operator.getHeartbeatrate());
		
		
		codeFragmentInfo.addCode(hashFragmentTemplate.getSt().render());
		codeFragmentInfo.addImport(HashFragmentPO.class.getName());
		
		return codeFragmentInfo;
	}

}