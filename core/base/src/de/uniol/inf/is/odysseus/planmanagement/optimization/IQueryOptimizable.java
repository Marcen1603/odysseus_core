package de.uniol.inf.is.odysseus.planmanagement.optimization;

import de.uniol.inf.is.odysseus.planmanagement.executor.IProvidesCompiler;
import de.uniol.inf.is.odysseus.planmanagement.executor.IProvidesQueries;

/**
 * Defines an object which provides informations for query optimization.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IQueryOptimizable extends IProvidesCompiler, IProvidesQueries{
}
