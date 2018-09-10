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
package de.uniol.inf.is.odysseus.recommendation.model.rating_predictor;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Cornelius Ludmann
 *
 */
public abstract class AbstractTupleBasedRatingPredictor<T extends Tuple<M>, M extends IMetaAttribute, U, I, P>
implements TupleBasedRatingPredictor<T, M, U, I, P> {

	/**
	 *
	 * Predicts a rating of a user-item pair.
	 *
	 * @param user
	 *            The user.
	 * @param item
	 *            The item.
	 * @return The rating prediction.
	 */
	@Override
	public abstract P predict(U user, I item);

	/**
	 * This implementation invokes
	 * {@link AbstractTupleBasedRatingPredictor#predict(U, I)} .
	 *
	 * @see RatingPredictor
	 */
	@Override
	public P predict(final T object, final SDFSchema inputschema,
			final SDFAttribute userAttribute, final SDFAttribute itemAttribute) {
		final int userAttributeIndex = inputschema.indexOf(userAttribute);
		final int itemAttributeIndex = inputschema.indexOf(itemAttribute);

		final U user = object.getAttribute(userAttributeIndex);
		final I item = object.getAttribute(itemAttributeIndex);
		return predict(user, item);
	}

	/**
	 * <p>
	 * This method uses the first attribute of <code>object</code> as user, the
	 * second as item. If user or item are not of type <code>U</code> resp.
	 * <code>I</code>, a {@link ClassCastException} is thrown. If object does
	 * not have two attributes, {@link IndexOutOfBoundsException} is thrown.
	 *
	 * <p>
	 * Consider to use
	 * {@link AbstractTupleBasedRatingPredictor#predict(Tuple, SDFSchema, SDFAttribute, SDFAttribute)}
	 * or {@link AbstractTupleBasedRatingPredictor#predict(U, I)} instead of
	 * this method.
	 *
	 * <p>
	 * This implementation invokes
	 * {@link AbstractTupleBasedRatingPredictor#predict(U, I)} .
	 *
	 * @see de.uniol.inf.is.odysseus.machine_learning.model.Model#predict(de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject)
	 */
	@Override
	public P predict(final T object) {
		final U user = object.getAttribute(0);
		final I item = object.getAttribute(1);
		return predict(user, item);
	}
}
