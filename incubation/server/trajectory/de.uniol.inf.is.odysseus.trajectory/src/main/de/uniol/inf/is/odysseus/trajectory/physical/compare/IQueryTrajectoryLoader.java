package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.Map;

import org.javatuples.Pair;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.test.RawTrajectory;
import de.uniol.inf.is.odysseus.trajectory.util.IObjectLoader;

public interface IQueryTrajectoryLoader extends IObjectLoader<RawTrajectory, String, Pair<Integer, Map<String, String>>> {

}
