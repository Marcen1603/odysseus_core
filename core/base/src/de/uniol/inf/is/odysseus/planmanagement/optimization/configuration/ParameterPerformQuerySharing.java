package de.uniol.inf.is.odysseus.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * Parameter der bestimmt, ob der Plan durch Query-Sharing umstrukturiert werden soll oder nicht.
 */
public class ParameterPerformQuerySharing extends Setting<Boolean> implements IOptimizationSetting<Boolean>, IQueryBuildSetting<Boolean> {
	/**
	 * Es soll Query-Sharing genutzt werden.
	 */
	public static final ParameterPerformQuerySharing TRUE = new ParameterPerformQuerySharing(true);
	
	/**
	 * Es soll kein Query-Sharing genutzt werden.
	 */
	public static final ParameterPerformQuerySharing FALSE = new ParameterPerformQuerySharing(false);

	/**
	 * Initialisiert ein neues ParamterPerformQuerySharing-Objekt
	 * 
	 * @param value
	 *            der Wert dieses Parameters
	 */
	public ParameterPerformQuerySharing(Boolean value) {
		super(value);
	}
}
