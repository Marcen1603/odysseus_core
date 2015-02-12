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
package de.uniol.inf.is.odysseus.machine_learning.learner;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.machine_learning.model.Model;

/**
 * A learner managed the learning data and trains a model.
 *
 * @author Cornelius Ludmann
 *
 * @param <T>
 *            Type of the learning data objects.
 * @param <M>
 *            Type of the metadata of the learning data objects.
 * @param <P>
 *            Type of the predictions.
 */
public interface Learner<T extends IStreamObject<M>, M extends IMetaAttribute, P> {

	/**
	 * Returns the name of the learner.
	 *
	 * @return The name of the learner.
	 */
	String getName();

	/**
	 * Adds a learning object to the learner.
	 *
	 * @param newLearningObject
	 *            The new learning object.
	 */
	void addLearningData(T newLearningObject);

	/**
	 * Adds a set of learning objects to the learner.
	 *
	 * @param newLearningObjects
	 *            The set of learning objects.
	 */
	void addLearningData(Set<T> newLearningObjects);

	/**
	 * Removes a learning object from the learner.
	 *
	 * @param oldLearningObject
	 *            The learning object that should be removed.
	 */
	void removeLearningData(T oldLearningObject);

	/**
	 * Remove a set of learning objects form the learner-
	 *
	 * @param oldLearningObjects
	 *            The learning objects that should be removed.
	 */
	void removeLearningData(Set<T> oldLearningObjects);

	/**
	 * Clears the learning data.
	 */
	void clear();

	/**
	 * Returns <code>true</code>, if the model is up to date, <code>false</code>
	 * otherwise. That means, after invoking {@link Learner#trainModel()} this
	 * method returns <code>true</code> as long as training data is added,
	 * removed, or the learner is cleared.
	 *
	 * @return <code>true</code>, if the model is up to date, <code>false</code>
	 *         otherwise.
	 */
	boolean isTrained();

	/**
	 * Trains the model with all known learning objects. After invoking this
	 * method, {@link Learner#isTrained()} should be returns true until learning
	 * data is added, removed, or the learner is cleared.
	 */
	void trainModel();

	/**
	 * Returns the trained model. <strong>It is strong recommended, that the
	 * returned model is immutable.</strong>
	 *
	 * @return The trained model or <code>null</code>, if no model exists.
	 */
	Model<T, M, P> getModel();

	/**
	 * Returns the trained model. <strong>It is strong recommended, that the
	 * returned model is immutable.</strong>
	 *
	 * @param train
	 *            <code>true</code>, if the model should be trained before (if
	 *            necessary), <code>false</code> otherwise.
	 * @return The trained model or <code>null</code>, if no model exists.
	 */
	Model<T, M, P> getModel(boolean train);

}
