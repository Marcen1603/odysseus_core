package de.uniol.inf.is.odysseus.peer.distribute.parameter;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class DoForceLocalParameter  extends Setting<Boolean> implements IQueryBuildSetting<Boolean>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 659950302188607911L;
	public static final DoForceLocalParameter TRUE = new DoForceLocalParameter(true);
	public static final DoForceLocalParameter FALSE = new DoForceLocalParameter(false);
	
	protected DoForceLocalParameter(Boolean value) {
		super(value);
	}
}
