package de.uniol.inf.is.odysseus.transformation.greedy.transformators.accessAO;

import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.TransformationException;

public class ExistingAccessAO implements IPOTransformator<AccessAO> {
	@Override
	public boolean canExecute(AccessAO accessAO, TransformationConfiguration config) {
		return WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI()) != null;
	}

	@Override
	public TransformedPO transform(AccessAO accessAO, TransformationConfiguration config, ITransformation transformation)
			throws TransformationException {
		ISource<?> accessPO = WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI());

//		MetadataCreationPO metadataCreationPO = AccessAOMetadata.createMetadata(accessPO, config);

		return new TransformedPO(accessPO);
//		return new TransformedPO(metadataCreationPO);
	}
	
	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public TempTransformationOperator createTempOperator() {
		TempTransformationOperator to = new TempTransformationOperator("ExistingAccessAO");
		return to;
	}
}
