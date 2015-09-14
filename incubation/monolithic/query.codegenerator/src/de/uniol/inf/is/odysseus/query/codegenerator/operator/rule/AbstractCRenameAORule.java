package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.NestedKeyValueObject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public abstract class AbstractCRenameAORule <T extends RenameAO> extends AbstractRule<RenameAO> {

	public AbstractCRenameAORule(String name) {
		super(name);
	}

	@Override
	public boolean isExecutable(RenameAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {
	
		return logicalOperator.getInputSchema().getType() != KeyValueObject.class && 
				logicalOperator.getInputSchema().getType() != NestedKeyValueObject.class;	
	

	}
	
	
}


