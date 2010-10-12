package de.uniol.inf.is.odysseus.planmanagement.executor.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;

/**
 * AbstractExecutionSetting is the base class for all execution settings.
 * 
 * @author Wolf Bauer
 * 
 * @param <E>
 *            Type of the setting value.
 */
public abstract class AbstractExecutionSetting<E> extends Setting<E> {

	protected AbstractExecutionSetting(E value) {
		super(value);
	}

}