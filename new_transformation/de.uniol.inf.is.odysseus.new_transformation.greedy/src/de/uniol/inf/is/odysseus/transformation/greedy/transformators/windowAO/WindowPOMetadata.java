package de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.window.AbstractWindowTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.window.SystemTimeIntervalFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataUpdater;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;

public class WindowPOMetadata {

	public static MetadataUpdatePO<?, ?> createMetadata(AbstractWindowTIPO<?> windowPO,
			TransformationConfiguration config) {
		WindowAO windowAO = windowPO.getWindowAO();
		if (windowAO.getWindowOn() == null) {
			IMetadataUpdater mFac = new SystemTimeIntervalFactory<ITimeInterval, RelationalTuple<ITimeInterval>>();
			MetadataUpdatePO mPO = new MetadataUpdatePO(mFac);
			mPO.subscribe(windowPO, 0, 0);
			return mPO;
		} else {
			if (config.getDataType() == "relational") {
				int attrPos = windowPO.getWindowAO().getInputSchema().indexOf(windowAO.getWindowOn());

				IMetadataUpdater mFac = new RelationalTimestampAttributeTimeIntervalMFactory(attrPos);

				MetadataUpdatePO mPO = new MetadataUpdatePO(mFac);
				mPO.subscribe(windowPO, 0, 0);
				return mPO;
			}
		}
		return null;
	}
}
