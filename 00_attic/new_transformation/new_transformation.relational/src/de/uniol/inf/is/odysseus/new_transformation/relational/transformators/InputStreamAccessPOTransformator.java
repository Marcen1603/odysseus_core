package de.uniol.inf.is.odysseus.new_transformation.relational.transformators;

import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.MetadataCreationPO;
import de.uniol.inf.is.odysseus.physicaloperator.access.IdentityTransformation;
import de.uniol.inf.is.odysseus.physicaloperator.access.InputStreamAccessPO;
import de.uniol.inf.is.odysseus.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.accessAO.AccessPOMetadata;

public class InputStreamAccessPOTransformator implements IPOTransformator<AccessAO> {
	@Override
	public boolean canExecute(AccessAO accessAO, TransformationConfiguration config) {
		String sourceType = accessAO.getSourceType();

		return (WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI()) == null)
				&& sourceType == "RelationalInputStreamAccessPO";
	}

	@Override
	public TransformedPO transform(AccessAO accessAO, TransformationConfiguration config, ITransformation transformation)
			throws TransformationException {
		ISource<?> accessPO = WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI());

		accessPO = new InputStreamAccessPO(accessAO.getHost(), accessAO.getPort(), new IdentityTransformation());
		String accessPOName = accessAO.getSource().getURI(false);
		WrapperPlanFactory.putAccessPlan(accessPOName, accessPO);

		MetadataCreationPO metadataCreationPO = AccessPOMetadata.createMetadata(accessPO, config);

		return new TransformedPO(metadataCreationPO);
	}
	
	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public TempTransformationOperator createTempOperator() {
		TempTransformationOperator to = new TempTransformationOperator("InputStreamAccessPO");
		return to;
	}
}
