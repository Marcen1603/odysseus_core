package de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;

/**
 * Query parameters can have a default root, that
 * is subscribed to all roots of a query. However,
 * sometimes, the root has to be cloned. In other
 * cases the root has not to be cloned so that
 * a multi-root query ends up in a single physical
 * operator and so on. This interface provides different strategies
 * for returning the default root of a query (clone or not).
 * 
 * @author André Bolles
 *
 */
public interface IDefaultRootStrategy {

	/**
	 * Subscribes the default root to the source.
	 * 
	 * @param defaultRoot
	 * @param source
	 * @return The default root, that has been subscribed. Some strategies
	 * may clone the default root, so that another reference has to be returned.
	 * This is necessary, since in Query.java the new root of the query has to
	 * set. However, if the subscribed object is already set, because it has not
	 * been cloned, we do not have to set it again. To check this we need the reference
	 * to the default root (clone or not).
	 */
	public IPhysicalOperator subscribeDefaultRootToSource(ISink<?> defaultRoot, IPhysicalOperator source);
}
