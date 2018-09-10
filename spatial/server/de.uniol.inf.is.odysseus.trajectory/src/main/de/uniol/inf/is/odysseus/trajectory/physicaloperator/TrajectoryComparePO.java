/*
 * Copyright 2015 Marcus Behrendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

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
import de.uniol.inf.is.odysseus.trajectory.compare.util.ITupleToRawTrajectoryConverter;
import de.uniol.inf.is.odysseus.trajectory.compare.util.TupleToRawTrajectoryConverterFactory;

/**
 * A physical operator for <tt>TrajectoryCompareAO</tt>.
 * 
 * @author marcus
 *
 * @param <T>
 *            the type of the processed data
 */
public class TrajectoryComparePO<T extends Tuple<ITimeInterval>> extends
		AbstractPipe<T, T> {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TrajectoryComparePO.class);

	public static final int VEHICLE_ID_POS = 0;

	public static final int POINTS_POS = 2;

	/** the algorithm to use for comparing trajectories in spatial dimension */
	private final ITrajectoryCompareAlgorithm<?, ?> algorithm;

	/** for converting tuples to <tt>RawTrajetories</tt> */
	private final ITupleToRawTrajectoryConverter tupleToRawTrajectoryConverter;

	/** the UTM zone of the trajectories */
	private final int utmZone;

	/**
	 * Creates an instance of <tt>TrajectoryComparePO</tt>.
	 * 
	 * @param k
	 *            the k-nearest trajectories to find
	 * @param queryTrajectoryPath
	 *            the file path to the query trajectory
	 * @param utmZone
	 *            the UTM zone of the trajectories
	 * @param lambda
	 *            importance between spatial and textual similarity
	 * @param algorithm
	 *            the algorithm to use for comparing trajectories in spatial
	 *            dimension
	 * @param textualAttributes
	 *            the textual attributes of the query trajectory
	 * @param options
	 *            options for the spatial compare algorithm
	 */
	public TrajectoryComparePO(final int k, final String queryTrajectoryPath,
			final int utmZone, final double lambda, final String algorithm,
			final Map<String, String> textualAttributes,
			final Map<String, String> options) {

		this.algorithm = TrajectoryCompareAlgorithmFactory.getInstance()
				.create(algorithm, k, queryTrajectoryPath, textualAttributes,
						utmZone, lambda, options);

		this.tupleToRawTrajectoryConverter = TupleToRawTrajectoryConverterFactory
				.getInstance().getProduct();
		this.utmZone = utmZone;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(final T object, final int port) {
		this.algorithm.removeBefore(object.getMetadata().getStart());

		final List<Tuple<?>> tupleList = object.getAttribute(2);
		if (tupleList.size() <= 1) {
			return;
		}

		final Tuple<ITimeInterval> result = new Tuple<ITimeInterval>(
				this.algorithm.getKNearest(this.tupleToRawTrajectoryConverter
						.convert(object, this.utmZone)), false);

		result.setMetadata(object.getMetadata());
		this.transfer((T) result);
	}

	@Override
	public void processPunctuation(final IPunctuation punctuation,
			final int port) {
		this.sendPunctuation(punctuation);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
}
