package de.uniol.inf.is.odysseus.transformation.relational_interval;

import de.uniol.inf.is.odysseus.intervalapproach.window.AbstractWindowTIPO;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataUpdater;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;

public class RelationalIntervalTransformationHelper {
	
	@SuppressWarnings("unchecked")
	public static MetadataUpdatePO initWindowOn(AbstractWindowTIPO windowPO){
		int attrPos = windowPO.getWindowAO().getInputSchema().indexOf(
				windowPO.getWindowAO().getWindowOn());
		
		IMetadataUpdater mFac = new RelationalTimestampAttributeTimeIntervalMFactory(
				attrPos);
		
		MetadataUpdatePO mPO = new MetadataUpdatePO( mFac );
		ISource bottom = (ISource)windowPO.getSubscribedTo(0).getTarget();
		bottom.unsubscribe(windowPO, 0, 0);
		bottom.subscribe(mPO, 0, 0);
		mPO.subscribe(windowPO, 0, 0);
		
		return mPO;
	}
}
