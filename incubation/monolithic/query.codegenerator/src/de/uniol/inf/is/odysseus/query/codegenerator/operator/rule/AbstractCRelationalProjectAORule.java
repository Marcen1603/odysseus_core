package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;

//TProjectAORule
public abstract class AbstractCRelationalProjectAORule extends AbstractRule {

	public AbstractCRelationalProjectAORule(String name) {
		super(name);
	}

	public boolean isExecutable(ILogicalOperator logicalOperator,
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

	public Class<?> getConditionClass() {
		return RelationalProjectPO.class;
	}

}
