package de.uniol.inf.is.odysseus.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * Parameter der bestimmt, ob der bereits laufende Plan durch Query-Sharing umstrukturiert werden soll,
 * oder nur neu hinzugekommene Queries ihre Operatoren getauscht bekommen dürfen.
 */
public class ParameterAllowRestructuringOfCurrentPlan extends Setting<Boolean> implements IOptimizationSetting<Boolean>, IQueryBuildSetting<Boolean> {
	/**
	 * Auch der alte Plan darf umstrukturiert werden.
	 */
	public static final ParameterAllowRestructuringOfCurrentPlan TRUE = new ParameterAllowRestructuringOfCurrentPlan(true);
	
	/**
	 * Nur neue Queries werden durch Query-Sharing optimiert.
	 */
	public static final ParameterAllowRestructuringOfCurrentPlan FALSE = new ParameterAllowRestructuringOfCurrentPlan(false);

	/**
	 * Initialisiert ein neues ParamterAllowRestructuringOfCurrentPlan-Objekt
	 * 
	 * @param value
	 *            der Wert dieses Parameters
	 */
	public ParameterAllowRestructuringOfCurrentPlan(Boolean value) {
		super(value);
	}
}
