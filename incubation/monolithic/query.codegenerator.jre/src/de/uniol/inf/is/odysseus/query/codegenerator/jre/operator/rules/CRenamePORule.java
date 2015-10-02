package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.TopPO;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCRenameAORule;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.DefaultCodegeneratorStatus;

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
		topPO.addImport(TopPO.class.getName());
		topPO.addImport(IStreamObject.class.getName());
		
		return topPO;
	}

}