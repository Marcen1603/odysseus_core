package de.uniol.inf.is.odysseus.transformation.relational_interval;

import de.uniol.inf.is.odysseus.intervalapproach.window.AbstractWindowTIPO;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataUpdater;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;

public class RelationalIntervalTransformationHelper {

	public static int getWindowOnAttrPos(AbstractWindowTIPO windowPO){
		return windowPO.getWindowAO().getInputSchema().indexOf(
					windowPO.getWindowAO().getWindowOn());
	}
	
	public static MetadataUpdatePO initWindowOn(AbstractWindowTIPO windowPO){
		int attrPos = windowPO.getWindowAO().getInputSchema().indexOf(
				windowPO.getWindowAO().getWindowOn());
		
		IMetadataUpdater mFac = new RelationalTimestampAttributeTimeIntervalMFactory(
				attrPos);
		
		System.out.println("WinOn 1");
		MetadataUpdatePO mPO = new MetadataUpdatePO( mFac );
		System.out.println("WinOn 2");
		ISource bottom = (ISource)windowPO.getSubscribedTo(0).getTarget();
		System.out.println("WinOn 3");
		bottom.unsubscribe(windowPO, 0, 0);
		System.out.println("WinOn 4");
		bottom.subscribe(mPO, 0, 0);
		System.out.println("WinOn 5");
		mPO.subscribe(windowPO, 0, 0);
		System.out.println("WinOn 6");
		
		return mPO;
	}
}
