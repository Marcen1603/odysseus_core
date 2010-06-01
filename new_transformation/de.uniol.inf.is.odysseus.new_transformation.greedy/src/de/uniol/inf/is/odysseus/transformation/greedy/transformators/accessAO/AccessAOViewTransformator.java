package de.uniol.inf.is.odysseus.transformation.greedy.transformators.accessAO;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;

public class AccessAOViewTransformator implements IPOTransformator<AccessAO> {
	@Override
	public boolean canExecute(AccessAO accessAO, TransformationConfiguration config) {
		String sourceURI = accessAO.getSource().getURI();

		return (WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI()) == null)
				&& DataDictionary.getInstance().getView(sourceURI) != accessAO;
	}

	@Override
	public TransformedPO transform(AccessAO accessAO, TransformationConfiguration config, ITransformation transformation)
			throws TransformationException {
		ISource<?> accessPO = WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI());
		String sourceURI = accessAO.getSource().getURI();

		ILogicalOperator view = DataDictionary.getInstance().getView(sourceURI);
		accessPO = (ISource) transformation.transform(view, config);
		String accessPOName = accessAO.getSource().getURI(false);
		WrapperPlanFactory.putAccessPlan(accessPOName, accessPO);

		return new TransformedPO(accessPO);

		// MetadataCreationPO metadataCreationPO =
		// AccessPOMetadata.createMetadata(accessPO, config);
		// return new TransformedPO(metadataCreationPO);
	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public TempTransformationOperator createTempOperator() {
		TempTransformationOperator to = new TempTransformationOperator("AccessAOView");
		return to;
	}
}
