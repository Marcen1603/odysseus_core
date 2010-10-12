package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;

/**
 * Base class for creating query build parameter. These parameter can be used to
 * provide additional information for building a query (e. g. special sinks as
 * default physical roots).
 * 
 * @author Wolf Bauer
 * 
 * @param <E>
 *            Defines the type of the value which could be stored in ths
 *            parameter (e. g. {@link IPhysicalOperator})
 */
public abstract class AbstractQueryBuildSetting<E> extends Setting<E> {

	protected AbstractQueryBuildSetting(E value) {
		super(value);
	}

}