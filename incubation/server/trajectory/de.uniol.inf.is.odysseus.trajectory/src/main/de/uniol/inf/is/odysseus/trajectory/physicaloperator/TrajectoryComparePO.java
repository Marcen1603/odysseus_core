package de.uniol.inf.is.odysseus.trajectory.physicaloperator;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.trajectory.compare.ITrajectoryCompareAlgorithm;
import de.uniol.inf.is.odysseus.trajectory.compare.TrajectoryCompareAlgorithmFactory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawIdTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.util.ITupleToRawTrajectoryConverter;
import de.uniol.inf.is.odysseus.trajectory.compare.util.TupleToRawTrajectoryConverterFactory;


// 
public class TrajectoryComparePO<T extends Tuple<ITimeInterval>> extends AbstractPipe<T, T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrajectoryComparePO.class);
		
	public static final int VEHICLE_ID_POS = 0;
	
	public static final int POINTS_POS = 2;

	private final ITrajectoryCompareAlgorithm<?, ?> algorithm;
	
	private final ITupleToRawTrajectoryConverter tupleToRawTrajectoryConverter;
	
	private final int utmZone;

	public TrajectoryComparePO(final int k, final String queryTrajectoryPath, final int utmZone, double lambda, final String algorithm, 
			Map<String, String> textualAttributes, Map<String, String> options) {
		
		this.algorithm = TrajectoryCompareAlgorithmFactory.getInstance().create(
				algorithm, k, queryTrajectoryPath, textualAttributes, utmZone, lambda, options);
		
		this.tupleToRawTrajectoryConverter = TupleToRawTrajectoryConverterFactory.getInstance().create();
		this.utmZone = utmZone;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		
		final RawIdTrajectory rawTrajectory = this.tupleToRawTrajectoryConverter.convert(object, this.utmZone);
		this.algorithm.removeBefore(object.getMetadata().getStart());
		List<?> knearest = this.algorithm.getKNearest(rawTrajectory);
		LOGGER.info(knearest.toString());
		
		final Tuple<ITimeInterval> result = new Tuple<ITimeInterval>(
				knearest,
				true
		);
		
		this.transfer((T)result);
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		this.sendPunctuation(punctuation);
	}
	
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
}
