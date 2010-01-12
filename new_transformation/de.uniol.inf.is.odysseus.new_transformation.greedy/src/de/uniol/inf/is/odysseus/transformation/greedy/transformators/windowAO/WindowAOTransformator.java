package de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO;

import java.util.Set;

import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.window.AbstractWindowTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingAdvanceTimeWindowTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingElementWindowTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingPeriodicWindowTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingTimeWindowTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.window.SystemTimeIntervalFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataUpdater;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;

public class WindowAOTransformator implements IPOTransformator<WindowAO> {
	@Override
	public boolean canExecute(WindowAO logicalOperator, TransformationConfiguration config) {
		Set<String> metaTypes = config.getMetaTypes();
		return (metaTypes.contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public TransformedPO transform(WindowAO windowAO, TransformationConfiguration config,
			ITransformation transformation) throws TransformationException {
		AbstractWindowTIPO windowPO = null;

		switch (windowAO.getWindowType()) {
		case JUMPING_TIME_WINDOW:
		case FIXED_TIME_WINDOW:
			windowPO = new SlidingAdvanceTimeWindowTIPO(windowAO);
			break;

		case SLIDING_TIME_WINDOW:
			windowPO = new SlidingTimeWindowTIPO(windowAO);
			break;

		case PERIODIC_TIME_WINDOW:
		case PERIODIC_TUPLE_WINDOW:
			windowPO = new SlidingPeriodicWindowTIPO(windowAO);
			break;

		case SLIDING_TUPLE_WINDOW:
		case JUMPING_TUPLE_WINDOW:
			windowPO = new SlidingElementWindowTIPO(windowAO);
			break;
		}

		if (windowPO == null)
			throw new TransformationException("WindowAO could not be transformed! WindowAO: " + windowAO);

		if (windowAO.getWindowOn() == null) {
			IMetadataUpdater mFac = new SystemTimeIntervalFactory<ITimeInterval, RelationalTuple<ITimeInterval>>();
			MetadataUpdatePO mPO = new MetadataUpdatePO(mFac);
			mPO.subscribe(windowPO, 0, 0);
			return new TransformedPO(windowPO, mPO);
		} else {
			if (config.getDataType() == "relational") {
				int attrPos = windowPO.getWindowAO().getInputSchema().indexOf(windowPO.getWindowAO().getWindowOn());

				IMetadataUpdater mFac = new RelationalTimestampAttributeTimeIntervalMFactory(attrPos);

				MetadataUpdatePO mPO = new MetadataUpdatePO(mFac);
				mPO.subscribe(windowPO, 0, 0);
				return new TransformedPO(windowPO, mPO);
			}
		}

		return new TransformedPO(windowPO);
	}
	
	@Override
	public int getPriority() {
		return 0;
	}
}
