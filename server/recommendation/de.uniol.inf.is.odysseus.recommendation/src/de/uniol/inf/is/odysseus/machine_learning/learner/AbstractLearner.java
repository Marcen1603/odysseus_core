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

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.machine_learning.model.Model;

/**
 * <p>
 * This is an abstract implementation of a learner. This implementation manages
 * the learning data by a {@link TreeSet}.
 *
 * <p>
 * For performance reasons, this implementation is not immutable. Consider to
 * pass save copies or immutables as learning data, if future changes of the
 * learning data should not affect this learner.
 *
 * @author Cornelius Ludmann
 *
 * @see Learner
 */
public abstract class AbstractLearner<T extends AbstractStreamObject<M>, M extends IMetaAttribute, P>
implements Learner<T, M, P> {

	protected Set<T> learningData = new LinkedHashSet<>();
	protected boolean isModelUpToDate = false;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.machine_learning.learner.Learner#getName()
	 */
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.Learner#addLearningData
	 * (de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject)
	 */
	@Override
	public void addLearningData(final T newLearningObject) {
		if (this.learningData.add(newLearningObject)) {
			this.isModelUpToDate = false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.Learner#addLearningData
	 * (java.util.Set)
	 */
	@Override
	public void addLearningData(final Set<T> newLearningObjects) {
		if (this.learningData.addAll(newLearningObjects)) {
			this.isModelUpToDate = false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.Learner#removeLearningData
	 * (de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject)
	 */
	@Override
	public void removeLearningData(final T oldLearningObject) {
		if (this.learningData.remove(oldLearningObject)) {
			this.isModelUpToDate = false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.Learner#removeLearningData
	 * (java.util.Set)
	 */
	@Override
	public void removeLearningData(final Set<T> oldLearningObjects) {
		if (this.learningData.removeAll(oldLearningObjects)) {
			this.isModelUpToDate = false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.machine_learning.learner.Learner#clear()
	 */
	@Override
	public void clear() {
		this.learningData.clear();
		this.isModelUpToDate = false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.Learner#isTrained()
	 */
	@Override
	public boolean isTrained() {
		return this.isModelUpToDate;
	}

	/**
	 * Returns the trained model. This implementation invokes
	 * {@link Learner#getModel(boolean)} with <code>train = true</code>.
	 *
	 * @see de.uniol.inf.is.odysseus.machine_learning.learner.Learner#getModel()
	 */
	@Override
	public Model<T, M, P> getModel() {
		return getModel(true);
	}

}
