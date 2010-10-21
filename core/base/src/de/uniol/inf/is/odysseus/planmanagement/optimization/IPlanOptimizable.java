package de.uniol.inf.is.odysseus.planmanagement.optimization;

import de.uniol.inf.is.odysseus.planmanagement.executor.IProvidesCompiler;
import de.uniol.inf.is.odysseus.planmanagement.executor.IProvidesQueries;

/**
 * Defines an object which provides informations for global plan optimization.
 * 
 * @author Wolf Bauer
 *
 */
public interface IPlanOptimizable extends IProvidesCompiler, IProvidesQueries{

}
