/**********************************************************************************
 * Copyright 2014 The Odysseus Team
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.RatingPredictor;
import de.uniol.inf.is.odysseus.recommendation.util.TupleSchemaHelper;

/**
 * @author Cornelius Ludmann
 *
 */
public class PredictRatingPO<M extends ITimeInterval, U, I> extends
		AbstractPipe<Tuple<M>, Tuple<M>> implements Cloneable {

	/**
	 * @author Cornelius Ludmann
	 *
	 */
	public enum PredcitRatingTupleSchema {
		USER, ITEM, MODEL
	}

	protected static Logger logger = LoggerFactory
			.getLogger(PredictRatingPO.class);

	private final TupleSchemaHelper<M, PredcitRatingTupleSchema> tupleSchemaHelper;

	public PredictRatingPO(
			final TupleSchemaHelper<M, PredcitRatingTupleSchema> tupleSchemaHelper) {
		this.tupleSchemaHelper = tupleSchemaHelper;
	}

	/**
	 * Copy constructor.
	 *
	 * @param recommendationPO
	 */
	public PredictRatingPO(final PredictRatingPO<M, U, I> recommendationPO) {
		super(recommendationPO);
		this.tupleSchemaHelper = recommendationPO.tupleSchemaHelper;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(final Tuple<M> object, final int port) {

		final U user = this.tupleSchemaHelper.getAttributeValue(object,
				PredcitRatingTupleSchema.USER);
		final I item = this.tupleSchemaHelper.getAttributeValue(object,
				PredcitRatingTupleSchema.ITEM);
		final RatingPredictor<Tuple<M>, M, U, I, Double> model = this.tupleSchemaHelper
				.getAttributeValue(object, PredcitRatingTupleSchema.MODEL);
		final double prediction = model.predict(user, item);

		final int attributeSize = object.getAttributes().length;
		final Tuple<M> tuple = new Tuple<M>(attributeSize, false);

		int j = 0;
		for (int i = 0; i < object.getAttributes().length; ++i) {

			// skip model attribute
			if (i != this.tupleSchemaHelper
					.getPos(PredcitRatingTupleSchema.MODEL)) {
				// copy all other attributes
				final Object attribute = object.getAttribute(i);
				tuple.setAttribute(j, attribute);
				++j;
			}
		}

		// add item as last attribute
		tuple.setAttribute(j, prediction);

		@SuppressWarnings("unchecked")
		final M metadata = (M) object.getMetadata().clone();
		tuple.setMetadata(metadata);
		transfer(tuple);

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
