package de.uniol.inf.is.odysseus.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;

/**
 * Setting for the AdvancedOptimizer.
 * 
 * @author Tobias Witt
 *
 * @param <E>
 */
public class AbstractOptimizationSetting<E> extends Setting<E> {

	protected AbstractOptimizationSetting(E value) {
		super(value);
	}
	

}
