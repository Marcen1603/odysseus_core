package de.uniol.inf.is.odysseus.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * Parameter der bestimmt, ob beim Query-Sharing nur identische Operatoren ersetzt werden,
 * oder auch die Zwischenergebnisse anderer ÄHNLICHER aber nicht identischer Operatoren bei der Optimierung
 * berücksichtigt werden sollen.
 */
public class ParameterShareSimilarOperators extends Setting<Boolean> implements IOptimizationSetting<Boolean>, IQueryBuildSetting<Boolean> {
	/**
	 * Auch teilweise gleiche Operatoren werden - wenn möglich - geteilt.
	 */
	public static final ParameterShareSimilarOperators TRUE = new ParameterShareSimilarOperators(true);
	
	/**
	 * Die Query-Sharing-Optimierung belässt es bei einem Austausch von identischen Operatoren
	 */
	public static final ParameterShareSimilarOperators FALSE = new ParameterShareSimilarOperators(false);

	/**
	 * Initialisiert ein neues ParamterAllowRestructuringOfCurrentPlan-Objekt
	 * 
	 * @param value
	 *            der Wert dieses Parameters
	 */
	public ParameterShareSimilarOperators(Boolean value) {
		super(value);
	}
}
