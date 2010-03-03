package de.uniol.inf.is.odysseus.new_transformation.relational.transformators;

import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataCreationPO;
import de.uniol.inf.is.odysseus.relational.base.access.AtomicDataInputStreamAccessPO;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.accessAO.AccessPOMetadata;


public class AtomicDataInputStreamAccessPOTransformator implements IPOTransformator<AccessAO> {
	@Override
	public boolean canExecute(AccessAO accessAO, TransformationConfiguration config) {
		String sourceType = accessAO.getSourceType();

		return (WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI()) == null)
				&& (sourceType == "RelationalAtomicDataInputStreamAccessPO" || sourceType == "RelationalStreaming");
	}

	@SuppressWarnings("unchecked")
	@Override
	public TransformedPO transform(AccessAO accessAO, TransformationConfiguration config, ITransformation transformation)
			throws TransformationException {
		ISource<?> accessPO = WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI());

		accessPO = new AtomicDataInputStreamAccessPO(accessAO.getHost(), accessAO.getPort(), accessAO.getOutputSchema());
		String accessPOName = accessAO.getSource().getURI(false);
		WrapperPlanFactory.putAccessPlan(accessPOName, accessPO);

		MetadataCreationPO metadataCreationPO = AccessPOMetadata.createMetadata(accessPO, config);

		return new TransformedPO(metadataCreationPO);
	}
	
	@Override
	public int getPriority() {
		return 0;
	}
}
