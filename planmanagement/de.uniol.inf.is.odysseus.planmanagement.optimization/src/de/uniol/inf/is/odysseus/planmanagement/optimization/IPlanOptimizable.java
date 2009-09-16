package de.uniol.inf.is.odysseus.planmanagement.optimization;

import java.util.List;

import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;

/**
 * Defines an object which provides informations for global plan optimization.
 * 
 * @author Wolf Bauer
 *
 */
public interface IPlanOptimizable {
	/**
	 * Returns a {@link ICompiler} for query processing like translating,
	 * rewriting and transforming.
	 * 
	 * @return {@link ICompiler} for query processing like translating,
	 *         rewriting and transforming
	 */
	public ICompiler getCompiler();

	/**
	 * Returns all currently registered queries in the system.
	 * 
	 * @return all currently registered queries in the system.
	 */
	public List<IEditableQuery> getRegisteredQueries();
}
