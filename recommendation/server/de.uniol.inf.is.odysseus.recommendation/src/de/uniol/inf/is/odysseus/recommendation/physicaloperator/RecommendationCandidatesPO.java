/**
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.recommendation.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.TuplePunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model.RecommendationCandidates;
import de.uniol.inf.is.odysseus.recommendation.util.TupleSchemaHelper;

/**
 * @author Cornelius Ludmann
 *
 */
public class RecommendationCandidatesPO<M extends ITimeInterval, U, I> extends
		AbstractPipe<Tuple<M>, Tuple<M>> {

	/**
	 * @author Cornelius Ludmann
	 *
	 */
	public enum RecommCandTupleSchema {
		USER, RATED_ITEMS_INFO
	}

	private final TupleSchemaHelper<M, RecommCandTupleSchema> tupleSchemaHelper;

	/**
	 *
	 */
	public RecommendationCandidatesPO(
			final TupleSchemaHelper<M, RecommCandTupleSchema> tupleSchemaHelper) {
		this.tupleSchemaHelper = tupleSchemaHelper;
	}

	/**
	 *
	 */
	public RecommendationCandidatesPO(
			final RecommendationCandidatesPO<M, U, I> po) {
		super(po);
		this.tupleSchemaHelper = po.tupleSchemaHelper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * getOutputMode()
	 */
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(final Tuple<M> object, final int port) {

		final U user = this.tupleSchemaHelper.getAttributeValue(object,
				RecommCandTupleSchema.USER);

		final RecommendationCandidates<U, I> ratedItemsInfo = this.tupleSchemaHelper
				.getAttributeValue(object,
						RecommCandTupleSchema.RATED_ITEMS_INFO);

		for (final I item : ratedItemsInfo.getRecommendationCandidates(user)) {

			final int attributeSize = object.getAttributes().length;
			final Tuple<M> tuple = new Tuple<M>(attributeSize, false);

			int j = 0;
			for (int i = 0; i < object.getAttributes().length; ++i) {
				// skip rated items info attribute
				if (i != this.tupleSchemaHelper
						.getPos(RecommCandTupleSchema.RATED_ITEMS_INFO)) {
					// copy all other attributes
					final Object attribute = object.getAttribute(i);
					tuple.setAttribute(j, attribute);
					++j;
				}
			}

			// add item as last attribute
			tuple.setAttribute(j, item);

			final M metadata = (M) object.getMetadata().clone();
			tuple.setMetadata(metadata);
			transfer(tuple);
		}
		final int rii = this.tupleSchemaHelper
				.getPos(RecommCandTupleSchema.RATED_ITEMS_INFO);
		this.tupleSchemaHelper.getPos(RecommCandTupleSchema.USER);
		final int[] restr = new int[object.getAttributes().length - 1];
		for (int i = 0, j = 0; i < object.getAttributes().length; ++i) {
			if (i != rii) {
				restr[j] = i;
				++j;
			}
		}
		sendPunctuation(new TuplePunctuation<Tuple<M>, M>(object
				.getMetadata().getStart(), object.restrict(restr, true)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.physicaloperator.ISink#processPunctuation
	 * (de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation, int)
	 */
	@Override
	public void processPunctuation(final IPunctuation punctuation,
			final int port) {
		sendPunctuation(punctuation, port);
	}
}
