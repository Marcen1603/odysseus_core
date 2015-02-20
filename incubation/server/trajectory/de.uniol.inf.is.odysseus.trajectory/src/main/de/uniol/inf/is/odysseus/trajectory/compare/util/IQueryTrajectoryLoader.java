package de.uniol.inf.is.odysseus.trajectory.compare.util;

import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.util.IObjectLoader;

/**
 * An extension to <tt>IObjectLoader</tt> in order to load <tt>RawQueryTrajectoriy</tt> 
 * from  a <i>file</i>.
 *  
 * @author marcus
 *
 */
public interface IQueryTrajectoryLoader extends IObjectLoader<RawQueryTrajectory, String, Integer> {

}
