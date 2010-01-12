package de.uniol.inf.is.odysseus.transformation.greedy.transformators.accessAO;

import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;

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
}
