package de.uniol.inf.is.odysseus.transformation.relational_interval;

import de.uniol.inf.is.odysseus.intervalapproach.window.AbstractWindowTIPO;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataUpdater;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class RelationalIntervalTransformationHelper {
	
	@SuppressWarnings("unchecked")
	public static MetadataUpdatePO initWindowOn(AbstractWindowTIPO windowPO){
		int attrPos = windowPO.getWindowAO().getInputSchema().indexOf(
				windowPO.getWindowAO().getWindowOn());
		
		IMetadataUpdater mFac = new RelationalTimestampAttributeTimeIntervalMFactory(
				attrPos);
		
		ISource bottom = (ISource)windowPO.getSubscribedToSource(0).getTarget();
		SDFAttributeList schema = windowPO.getSubscribedToSource(0).getSchema();
		MetadataUpdatePO mPO = new MetadataUpdatePO( mFac );
		mPO.setOutputSchema(schema);
		bottom.unsubscribeSink(windowPO, 0, 0, schema);
		bottom.subscribeSink(mPO, 0, 0, schema);
		mPO.subscribeSink(windowPO, 0, 0, schema);
		
		return mPO;
	}
}
