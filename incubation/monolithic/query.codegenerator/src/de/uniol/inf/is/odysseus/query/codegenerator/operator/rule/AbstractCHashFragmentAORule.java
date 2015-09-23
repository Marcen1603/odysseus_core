package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;

public abstract class AbstractCHashFragmentAORule<T extends HashFragmentAO> extends AbstractRule<HashFragmentAO> {

	public AbstractCHashFragmentAORule(String name) {
		super(name);
	}

	public boolean isExecutable(HashFragmentAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

			return true;
	}



}