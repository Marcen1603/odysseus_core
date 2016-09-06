package de.uniol.inf.is.odysseus.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;
/**
 * abstract rule for the HashFragmentAO
 * 
 * @author MarcPreuschaft
 *
 * @param <T>
 */
public abstract class AbstractCHashFragmentAORule<T extends HashFragmentAO> extends AbstractCOperatorRule<HashFragmentAO> {

	public AbstractCHashFragmentAORule(String name) {
		super(name);
	}

	public boolean isExecutable(HashFragmentAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

			return true;
	}



}