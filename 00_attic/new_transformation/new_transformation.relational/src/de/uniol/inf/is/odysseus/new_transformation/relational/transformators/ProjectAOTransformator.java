package de.uniol.inf.is.odysseus.new_transformation.relational.transformators;

import de.uniol.inf.is.odysseus.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.TransformationException;

public class ProjectAOTransformator implements IPOTransformator<ProjectAO> {
	@Override
	public boolean canExecute(ProjectAO logicalOperator, TransformationConfiguration config) {
		return config.getDataType() == "relational";
	}
	
	@Override
	public TransformedPO transform(ProjectAO projectAO, TransformationConfiguration config, ITransformation transformation)
			throws TransformationException {

		RelationalProjectPO<?> projectPO = new RelationalProjectPO(projectAO.determineRestrictList());
		return new TransformedPO(projectPO);
	}
	
	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public TempTransformationOperator createTempOperator() {
		TempTransformationOperator to = new TempTransformationOperator("ProjectAO");
		return to;
	}
}
