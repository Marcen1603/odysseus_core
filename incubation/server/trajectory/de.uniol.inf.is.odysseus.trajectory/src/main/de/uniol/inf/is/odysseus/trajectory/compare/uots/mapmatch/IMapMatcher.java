package de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch;

import java.util.Map;

import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.data.UotsDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.data.UotsQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.graph.NetGraph;

/**
 * An object which converts a <tt>RawDataTrajectory</tt> or a <tt>RawQueryTrajectory</tt> to
 * an <tt>UotsDataTrajectory</tt> or an <tt>UotsQueryTrajectory</tt>, respectively by mapping
 * their <tt>Points</tt> to <i>vertices</i> of a <tt>NetGraph's</tt> <i>reduced graph</i>.
 * 
 * @author marcus
 *
 */
public interface IMapMatcher {

	/**
	 * Maps the raw points of the passed <tt>RawDataTrajectory</tt> to <i>vertices</i> of 
	 * the passed <tt>NetGraph's</tt> <i>reduced graph</i> and returns the resulting
	 * <tt>UotsDataTrajectory</tt>.
	 * 
	 * @param trajectory the <tt>RawDataTrajectory</tt> which's raw point will be mapped to
	 * 		  the <i>vertices</i> of the <tt>NetGraph's</tt> <i>reduced graph</i>
	 * @param graph the <tt>NetGraph's</tt> <i>reduced graph</i> is utilized to map the trajectories
	 * 	      raw points to <i>vertices</i>
	 * @return the resulting <tt>UotsDataTrajectory</tt> by mapping the points
	 * @throws IllegalArgumentException if <tt>trajectory == null</tt> or <tt>graph == null</tt>
	 */
	public UotsDataTrajectory map(final RawDataTrajectory trajectory, final NetGraph graph) throws IllegalArgumentException;
	
	/**
	 * Maps the raw points of the passed <tt>RawQueryTrajectory</tt> to <i>vertices</i> of 
	 * the passed <tt>NetGraph's</tt> <i>reduced graph</i> and returns the resulting
	 * <tt>UotsDataTrajectory</tt>. Additionally, the textual attributes has to be passed
	 * since a <tt>RawQueryTrajectory</tt> has none.
	 * 
	 * @param trajectory the <tt>RawDataTrajectory</tt> which's raw point will be mapped to
	 * 		  the <i>vertices</i> of the <tt>NetGraph's</tt> <i>reduced graph</i>
	 * @param textualAttributes the textual attributes the resulting <tt>UotsQueryTrajectory</tt> will contain
	 * @param graph the <tt>NetGraph's</tt> <i>reduced graph</i> is utilized to map the trajectories
	 * 	      raw points to <i>vertices</i>
	 * @return the resulting <tt>UotsDataTrajectory</tt> by mapping the points
	 * @throws IllegalArgumentException if <tt>trajectory == null</tt> or <tt>graph == null</tt>
	 */
	public UotsQueryTrajectory map(final RawQueryTrajectory trajectory, final Map<String, String> textualAttributes, final NetGraph graph) 
		throws IllegalArgumentException;
}
