package de.uniol.inf.is.odysseus.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;

/**
 * Sets the number of candidate plans that should be compared.
 * 
 * @author Tobias Witt
 *
 */
public class SettingComparePlanCandidates extends Setting<Integer> implements IOptimizationSetting<Integer> {

	public SettingComparePlanCandidates(Integer value) {
		super(value);
	}

}
