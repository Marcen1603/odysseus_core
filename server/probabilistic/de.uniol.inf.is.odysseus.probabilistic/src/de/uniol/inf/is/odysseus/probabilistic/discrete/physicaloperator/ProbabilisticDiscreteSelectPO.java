/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.probabilistic.common.ProbabilisticDiscreteUtils;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;

/**
 * Implementation of a probabilistic Select operator for discrete probabilistic values.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @param <T>
 */
public class ProbabilisticDiscreteSelectPO<T extends Tuple<?>> extends SelectPO<T> {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(ProbabilisticDiscreteSelectPO.class);
	/** Positions of probabilistic attributes. */
	private final int[] probabilisticAttributePos;

	/**
	 * Default constructor.
	 * 
	 * @param predicate
	 *            The predicate
	 * @param probabilisticAttributePos
	 *            The positions of discrete probabilistic attributes
	 */
	public ProbabilisticDiscreteSelectPO(final IPredicate<? super T> predicate, final int[] probabilisticAttributePos) {
		super(predicate);
		this.probabilisticAttributePos = probabilisticAttributePos;
	}

	/**
	 * Clone constructor.
	 * 
	 * @param po
	 *            The copy
	 */
	public ProbabilisticDiscreteSelectPO(final ProbabilisticDiscreteSelectPO<T> po) {
		super(po);
		this.probabilisticAttributePos = po.probabilisticAttributePos.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe# process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected final void process_next(final T object, final int port) {
		final T outputVal = (T) object.clone();
		// Dummy tuple to hold the different worlds during evaluation
		final T selectObject = (T) object.clone();

		// Input and output joint probabilities
		final double[] inSum = new double[this.probabilisticAttributePos.length];
		final double[] outSum = new double[this.probabilisticAttributePos.length];

		for (int i = 0; i < this.probabilisticAttributePos.length; i++) {
			((AbstractProbabilisticValue<?>) outputVal.getAttribute(this.probabilisticAttributePos[i])).getValues().clear();
			final AbstractProbabilisticValue<?> attribute = (AbstractProbabilisticValue<?>) object.getAttribute(this.probabilisticAttributePos[i]);
			for (final Double probability : attribute.getValues().values()) {
				inSum[i] += probability;
			}
		}

		final Object[][] worlds = ProbabilisticDiscreteUtils.getWorlds(object, this.probabilisticAttributePos);

		// Evaluate each world and store the possible ones in the output tuple
		for (int w = 0; w < worlds.length; w++) {
			for (int i = 0; i < this.probabilisticAttributePos.length; i++) {
				selectObject.setAttribute(this.probabilisticAttributePos[i], worlds[w][i]);
			}
			final boolean result = this.getPredicate().evaluate(selectObject);

			if (result) {
				for (int i = 0; i < this.probabilisticAttributePos.length; i++) {
					final AbstractProbabilisticValue<?> inAttribute = (AbstractProbabilisticValue<?>) object.getAttribute(this.probabilisticAttributePos[i]);
					final AbstractProbabilisticValue<Double> outAttribute = (AbstractProbabilisticValue<Double>) outputVal.getAttribute(this.probabilisticAttributePos[i]);
					final double probability = inAttribute.getValues().get(worlds[w][i]);
					if (!outAttribute.getValues().containsKey(worlds[w][i])) {
						outAttribute.getValues().put((Double) worlds[w][i], probability);
						outSum[i] += probability;
					}
				}
			}
		}
		// Update the joint probability
		double jointProbability = ((IProbabilistic) outputVal.getMetadata()).getExistence();
		for (int i = 0; i < this.probabilisticAttributePos.length; i++) {
			jointProbability /= inSum[i];
			jointProbability *= outSum[i];
		}
		// Transfer the tuple iff the joint probability is positive (maybe set quality filter later to reduce the number of tuples)
		if (jointProbability > 0.0) {
			((IProbabilistic) outputVal.getMetadata()).setExistence(jointProbability);
			// KTHXBYE
			this.transfer(outputVal);
		} else if (ProbabilisticDiscreteSelectPO.LOG.isTraceEnabled()) {
			ProbabilisticDiscreteSelectPO.LOG.trace("Drop tuple: " + outputVal.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#clone ()
	 */
	@Override
	public final ProbabilisticDiscreteSelectPO<T> clone() {
		return new ProbabilisticDiscreteSelectPO<T>(this);
	}

}
