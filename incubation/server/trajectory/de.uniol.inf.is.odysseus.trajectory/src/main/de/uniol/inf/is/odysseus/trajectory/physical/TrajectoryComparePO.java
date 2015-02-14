package de.uniol.inf.is.odysseus.trajectory.physical;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.AbstractDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.ITrajectoryCompareAlgorithm;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.ITupleToRawTrajectoryConverter;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.TrajectoryCompareAlgorithmFactory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.TupleToRawTrajectoryConverterFactory;


// 
public class TrajectoryComparePO<T extends Tuple<ITimeInterval>> extends AbstractPipe<T, T> {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(TrajectoryComparePO.class);
		
	public static final int VEHICLE_ID_POS = 0;
	
	public static final int POINTS_POS = 2;

	private final ITrajectoryCompareAlgorithm algorithm;
	
	private final ITupleToRawTrajectoryConverter tupleToRawTrajectoryConverter;
	
	private final int utmZone;

	public TrajectoryComparePO(final int k, final String queryTrajectoryPath, final int utmZone, final String algorithm, Map<String, String> options) {
		this.algorithm = TrajectoryCompareAlgorithmFactory.getInstance().create(algorithm, k, queryTrajectoryPath, utmZone, options);
		this.tupleToRawTrajectoryConverter = TupleToRawTrajectoryConverterFactory.getInstance().create();
		this.utmZone = utmZone;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		final RawDataTrajectory rawTrajectory = this.tupleToRawTrajectoryConverter.convert(object, this.utmZone);
		this.algorithm.removeBefore(object.getMetadata().getStart());
		
		List<AbstractDataTrajectory> knearest = this.algorithm.getKNearest(rawTrajectory);
		LOGGER.info(knearest.toString());
		
		final Tuple<ITimeInterval> result = new Tuple<ITimeInterval>(
				knearest,
				true
		);
		this.transfer((T)result);
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	public final class R4d {
		String id;
		
		double distance;
		
		public R4d(String id, double distance) {
			super();
			this.id = id;
			this.distance = distance;
		}

		public String toString() {
			return "[" + id + " " + distance;
		}
	}
}
