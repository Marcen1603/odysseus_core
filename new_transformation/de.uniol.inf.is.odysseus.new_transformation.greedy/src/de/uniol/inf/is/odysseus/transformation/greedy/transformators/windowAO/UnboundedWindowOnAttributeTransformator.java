package de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO;

import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowType;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataUpdater;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class UnboundedWindowOnAttributeTransformator implements IPOTransformator<WindowAO> {

	@Override
	public boolean canExecute(WindowAO windowAO, TransformationConfiguration config) {
		return config.getDataType() == "relational"
				&& config.getMetaTypes().contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval")
				&& windowAO.getWindowType() == WindowType.UNBOUNDED && windowAO.getWindowOn() != null;
	}

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public TransformedPO transform(WindowAO windowAO, TransformationConfiguration config, ITransformation transformation)
			throws TransformationException {

		SDFAttributeList schema = windowAO.getInputSchema();
		int attrPos = schema.indexOf(windowAO.getWindowOn());

		IMetadataUpdater mFac = new RelationalTimestampAttributeTimeIntervalMFactory(attrPos);

		// ISource bottom = windowAO.getPhysInputPOs().iterator().next();
		MetadataUpdatePO mPO = new MetadataUpdatePO(mFac);
		mPO.setOutputSchema(schema);

		return new TransformedPO(mPO);
	}

	@Override
	public TempTransformationOperator createTempOperator() {
		TempTransformationOperator to = new TempTransformationOperator("UnboundedWindowOnAttribute");
		return to;
	}
}
