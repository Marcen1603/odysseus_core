package de.uniol.inf.is.odysseus.transformation.relational_interval;

import de.uniol.inf.is.odysseus.intervalapproach.window.AbstractWindowTIPO;

public class TransformationHelper {

	public static int getWindowOnAttrPos(AbstractWindowTIPO windowPO){
		return windowPO.getWindowAO().getInputSchema().indexOf(
					windowPO.getWindowAO().getWindowOn());
	}
}
