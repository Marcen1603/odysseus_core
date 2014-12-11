package de.uniol.inf.is.odysseus.trajectory.physical;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;


// 
public class TrajectoryComparePO<T extends Tuple<ITimeInterval>> extends AbstractPipe<T, T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrajectoryComparePO.class);
	
	public TrajectoryComparePO(int k, List<String> queryTrajectory, String referenceSystem) {
	}
	
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	// Ein beliebiges Tupel
	@Override
	protected void process_next(T object, int port) {
	
		
		this.transfer((T) object, port);
	}
}
