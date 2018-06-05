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
public interface TupleBasedRatingPredictor<T extends Tuple<M>, M extends IMetaAttribute, U, I, P>
extends RatingPredictor<T, M, U, I, P> {

	/**
	 * Predicts a rating of a user-item pair.
	 *
	 * @param object
	 *            The tuple that holds the user and item.
	 * @param inputschema
	 *            The input schema of the tuple.
	 * @param userAttribute
	 *            The user attribute in <code>tuple</code>.
	 * @param itemAttribute
	 *            The item attribute in <code>tuple</code>.
	 * @return The rating prediction.
	 */
	P predict(T object, SDFSchema inputschema, SDFAttribute userAttribute,
			SDFAttribute itemAttribute);
}
