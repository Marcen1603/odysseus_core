package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCRenameAORule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.TopPO;

/**
 * This rule generate from a RenameAO the code for the 
 * TopPO operator. 
 * 
 * template: operator/topPO.st
 * 
 * @author MarcPreuschaft
 *
 */
public class CRenamePORule extends AbstractCRenameAORule<RenameAO>{

	public CRenamePORule() {
		super(CRenamePORule.class.getName());
	}


	@Override
	public CodeFragmentInfo getCode(RenameAO operator) {
		CodeFragmentInfo topPO = new CodeFragmentInfo();
		
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);
		
		StringTemplate topTemplate = new StringTemplate("operator","topPO");
		topTemplate.getSt().add("operatorVariable", operatorVariable);
		topPO.addCode(topTemplate.getSt().render());
		
		topPO.addFrameworkImport(TopPO.class.getName());
		topPO.addFrameworkImport(IStreamObject.class.getName());
		
		return topPO;
	}

}