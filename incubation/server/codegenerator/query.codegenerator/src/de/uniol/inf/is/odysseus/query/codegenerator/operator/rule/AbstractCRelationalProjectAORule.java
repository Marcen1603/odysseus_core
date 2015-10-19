package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

//TProjectAORule
public abstract class AbstractCRelationalProjectAORule<T extends ProjectAO> extends AbstractCOperatorRule<ProjectAO> {

	public AbstractCRelationalProjectAORule(String name) {
		super(name);
	}

	@Override
	public boolean isExecutable(ProjectAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {


			if (logicalOperator.getInputSchema().getType() == Tuple.class) {
				return true;
			}

			return false;
	

	}



}
