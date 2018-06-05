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
package de.uniol.inf.is.odysseus.machine_learning.model;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

/**
 * A model gives a prediction for an object.
 *
 * @author Cornelius Ludmann
 *
 * @param <T>
 *            Type of the objects.
 * @param <M>
 *            Type of the metadata of the objects.
 * @param <P>
 *            Type of the predictions.
 */
public interface Model<T extends IStreamObject<M>, M extends IMetaAttribute, P> {

	/**
	 * Returns a predicts for an object.
	 *
	 * @param object
	 *            The object for which the prediction should be calculated.
	 * @return The prediction.
	 */
	P predict(T object);
}
