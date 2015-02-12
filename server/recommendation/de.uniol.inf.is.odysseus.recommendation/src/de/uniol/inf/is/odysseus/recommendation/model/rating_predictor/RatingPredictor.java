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
package de.uniol.inf.is.odysseus.recommendation.model.rating_predictor;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.machine_learning.model.Model;

/**
 * A {@linkplain RatingPredictor} predicts ratings for a user-item-pair.
 *
 * @author Cornelius Ludmann
 */
public interface RatingPredictor<T extends IStreamObject<M>, M extends IMetaAttribute, U, I, P>
extends Model<T, M, P> {

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
	P predict(U user, I item);

}
