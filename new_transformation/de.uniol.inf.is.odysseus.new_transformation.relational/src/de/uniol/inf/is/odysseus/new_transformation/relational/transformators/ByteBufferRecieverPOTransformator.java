package de.uniol.inf.is.odysseus.new_transformation.relational.transformators;

import java.io.IOException;

import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataCreationPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.ByteBufferReceiverPO;
import de.uniol.inf.is.odysseus.relational.base.RelationalTupleObjectHandler;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.accessAO.AccessPOMetadata;

public class ByteBufferRecieverPOTransformator implements IPOTransformator<AccessAO> {
	@Override
	public boolean canExecute(AccessAO accessAO, TransformationConfiguration config) {
		String sourceType = accessAO.getSourceType();

		return (WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI()) == null)
				&& sourceType == "RelationalByteBufferAccessPO";
	}

	@SuppressWarnings("unchecked")
	@Override
	public TransformedPO transform(AccessAO accessAO, TransformationConfiguration config, ITransformation transformation)
			throws TransformationException {
		ISource<?> accessPO = WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI());

		try {
			accessPO = new ByteBufferReceiverPO(new RelationalTupleObjectHandler(accessAO.getOutputSchema()), accessAO
					.getHost(), accessAO.getPort());
		} catch (IOException e) {
			throw new TransformationException(e);
		}
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
