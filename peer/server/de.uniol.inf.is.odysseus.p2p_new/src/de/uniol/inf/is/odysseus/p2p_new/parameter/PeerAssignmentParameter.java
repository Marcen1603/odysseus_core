package de.uniol.inf.is.odysseus.p2p_new.parameter;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.IOptimizationSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * This class represents the setting for the peer assignment strategies. <br />
 * In Odysseus Script it can be used as follows: <br />
 * #PEERASSIGNMENT name
 * @author Michael Brand
 */
public class PeerAssignmentParameter extends Setting<String> implements
		IOptimizationSetting<String>, IQueryBuildSetting<String> {
	
	/**
	 * The string representation for an undefined strategy.
	 */
	public static final String UNDEFINED = "undefined";

	/**
	 * Constructs a new parameter "PEERASSIGNMENT".
	 * @param value The value for the parameter.
	 */
	public PeerAssignmentParameter(String value) {
		
		super(value);

	}

}