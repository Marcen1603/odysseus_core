package de.uniol.inf.is.odysseus.trajectory.compare.util;

import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.util.IObjectLoader;

/**
 * An extension to <tt>IObjectLoader</tt> to load <tt>RawQueryTrajectory</tt> from <i>files</i>.
 *  
 * @author marcus
 *
 */
public interface IQueryTrajectoryLoader extends IObjectLoader<RawQueryTrajectory, String, Integer> {

}
