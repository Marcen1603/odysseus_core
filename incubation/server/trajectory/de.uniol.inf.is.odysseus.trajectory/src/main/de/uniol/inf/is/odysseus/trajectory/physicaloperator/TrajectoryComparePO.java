package de.uniol.inf.is.odysseus.trajectory.physicaloperator;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.trajectory.compare.ITrajectoryCompareAlgorithm;
import de.uniol.inf.is.odysseus.trajectory.compare.TrajectoryCompareAlgorithmFactory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.util.ITupleToRawTrajectoryConverter;
import de.uniol.inf.is.odysseus.trajectory.compare.util.TupleToRawTrajectoryConverterFactory;


/**
 * 
 * @author marcus
 *
 * @param <T>
 */
public class TrajectoryComparePO<T extends Tuple<ITimeInterval>> extends AbstractPipe<T, T> {

	@SuppressWarnings("unused")
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
		
		this.tupleToRawTrajectoryConverter = TupleToRawTrajectoryConverterFactory.getInstance().getProduct();
		this.utmZone = utmZone;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		
		final RawDataTrajectory rawTrajectory = this.tupleToRawTrajectoryConverter.convert(object, this.utmZone);
		this.algorithm.removeBefore(object.getMetadata().getStart());
		
		final Tuple<ITimeInterval> result = new Tuple<ITimeInterval>(
				this.algorithm.getKNearest(rawTrajectory),
				true
		);
		
		result.setMetadata(object.getMetadata());
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
