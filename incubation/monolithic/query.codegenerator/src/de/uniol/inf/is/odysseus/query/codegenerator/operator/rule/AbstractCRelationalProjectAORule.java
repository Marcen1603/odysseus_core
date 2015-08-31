package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

//TProjectAORule
public abstract class AbstractCRelationalProjectAORule<T extends ProjectAO> extends AbstractRule<ProjectAO> {

	public AbstractCRelationalProjectAORule(String name) {
		super(name);
	}

	public boolean isExecutable(ProjectAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

		if (logicalOperator instanceof ProjectAO) {
			ProjectAO operator = (ProjectAO) logicalOperator;

			if (operator.getInputSchema().getType() == Tuple.class) {
				return true;
			}

			return false;
		}

		return false;

	}



}
