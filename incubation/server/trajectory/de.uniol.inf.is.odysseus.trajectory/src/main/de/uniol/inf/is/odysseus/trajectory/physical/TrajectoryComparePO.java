package de.uniol.inf.is.odysseus.trajectory.physical;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.ITrajectoryCompareAlgorithm;


// 
public class TrajectoryComparePO<T extends Tuple<ITimeInterval>> extends AbstractPipe<T, T> {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(TrajectoryComparePO.class);
	
	public static final int VEHICLE_ID_POS = 0;
	
	public static final int POINTS_POS = 1;
	

	private final ITrajectoryCompareAlgorithm algorithm;
	
	
	public TrajectoryComparePO(ITrajectoryCompareAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		this.transfer((T) this.algorithm.getKNearest(object));
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
}
