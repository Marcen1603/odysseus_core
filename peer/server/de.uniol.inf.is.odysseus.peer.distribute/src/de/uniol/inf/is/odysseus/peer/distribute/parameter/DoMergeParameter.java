package de.uniol.inf.is.odysseus.peer.distribute.parameter;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class DoMergeParameter  extends Setting<Boolean> implements IQueryBuildSetting<Boolean>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7936800279103411909L;
	public static final DoMergeParameter TRUE = new DoMergeParameter(true);
	public static final DoMergeParameter FALSE = new DoMergeParameter(false);
	
	protected DoMergeParameter(Boolean value) {
		super(value);
	}
}
