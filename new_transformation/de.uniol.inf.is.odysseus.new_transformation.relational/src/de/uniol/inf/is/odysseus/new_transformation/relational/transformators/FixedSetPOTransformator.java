package de.uniol.inf.is.odysseus.new_transformation.relational.transformators;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.relational.FixedSetAccessAO;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.new_transformation.stream_characteristics.StreamCharacteristicCollection;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataCreationPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.FixedSetPO;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.accessAO.AccessPOMetadata;

public class FixedSetPOTransformator implements IPOTransformator<AccessAO> {
	@Override
	public boolean canExecute(AccessAO accessAO, TransformationConfiguration config) {
		return (WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI()) == null)
				&& accessAO instanceof FixedSetAccessAO;
	}

	@Override
	public TransformedPO transform(AccessAO accessAO, TransformationConfiguration config, ITransformation transformation)
			throws TransformationException {
		ISource<?> accessPO = WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI());

		FixedSetAccessAO fixedSetAccessAO = (FixedSetAccessAO) accessAO;
		accessPO = new FixedSetPO(fixedSetAccessAO.getTuples());
		String accessPOName = accessAO.getSource().getURI(false);
		WrapperPlanFactory.putAccessPlan(accessPOName, accessPO);

		MetadataCreationPO metadataCreationPO = AccessPOMetadata.createMetadata(accessPO, config);

		return new TransformedPO(metadataCreationPO);
	}
	
	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public TempTransformationOperator createTempOperator() {
		TempTransformationOperator to = new TempTransformationOperator("FixedSetPO");
		return to;
	}
}
