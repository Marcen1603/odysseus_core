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
package de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
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
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ProbabilisticDiscreteSelectPO.class);

	private int[] probabilisticAttributePos;

	/**
	 * Default constructor.
	 * 
	 * @param predicate
	 *            The predicate
	 * @param probabilisticAttributePos
	 *            The positions of discrete probabilistic attributes
	 */
	public ProbabilisticDiscreteSelectPO(final IPredicate<? super T> predicate, int[] probabilisticAttributePos) {
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
		T outputVal = (T) object.clone();
		// Dummy tuple to hold the different worlds during evaluation
		T selectObject = (T) object.clone();

		// Input and output joint probabilities
		double[] inSum = new double[probabilisticAttributePos.length];
		double[] outSum = new double[probabilisticAttributePos.length];

		Iterator<?>[] attributeIters = new Iterator<?>[probabilisticAttributePos.length];
		int worldNum = 1;
		for (int i = 0; i < probabilisticAttributePos.length; i++) {
			((AbstractProbabilisticValue<?>) outputVal.getAttribute(probabilisticAttributePos[i])).getValues().clear();
			AbstractProbabilisticValue<?> attribute = (AbstractProbabilisticValue<?>) object.getAttribute(probabilisticAttributePos[i]);
			worldNum *= attribute.getValues().size();
			for (Double proberbility : attribute.getValues().values()) {
				inSum[i] += proberbility;
			}
			attributeIters[i] = attribute.getValues().entrySet().iterator();

		}

		// Create all possible worlds
		Object[][] worlds = new Object[worldNum][probabilisticAttributePos.length];
		double instances = 0;
		for (int i = 0; i < probabilisticAttributePos.length; i++) {
			AbstractProbabilisticValue<?> attribute = (AbstractProbabilisticValue<?>) object.getAttribute(probabilisticAttributePos[i]);
			int world = 0;
			if (instances == 0.0) {
				instances = 1.0;
			} else {
				instances *= attribute.getValues().size();
			}
			int num = (int) (worlds.length / (attribute.getValues().size() * instances));
			while (num > 0) {
				Iterator<?> iter = attribute.getValues().entrySet().iterator();
				while (iter.hasNext()) {
					Entry<?, Double> entry = ((Map.Entry<?, Double>) iter.next());
					for (int j = 0; j < instances; j++) {
						if (world == worlds.length) {
							LOG.error("Strange things happening in the world");
						}
						worlds[world][i] = entry.getKey();
						world++;
					}
				}
				num--;
			}
		}

		// Evaluate each world and store the possible ones in the output tuple
		for (int w = 0; w < worlds.length; w++) {
			for (int i = 0; i < probabilisticAttributePos.length; i++) {
				selectObject.setAttribute(probabilisticAttributePos[i], worlds[w][i]);
			}
			boolean result = getPredicate().evaluate((T) selectObject);

			if (result) {
				for (int i = 0; i < probabilisticAttributePos.length; i++) {
					AbstractProbabilisticValue<?> inAttribute = (AbstractProbabilisticValue<?>) object.getAttribute(probabilisticAttributePos[i]);
					AbstractProbabilisticValue<Double> outAttribute = (AbstractProbabilisticValue<Double>) outputVal.getAttribute(probabilisticAttributePos[i]);
					double probability = inAttribute.getValues().get(worlds[w][i]);
					outAttribute.getValues().put((Double) worlds[w][i], probability);
					outSum[i] += probability;
				}
			}
		}
		// Update the joint probability
		double jointProbability = ((IProbabilistic) outputVal.getMetadata()).getExistence();
		for (int i = 0; i < probabilisticAttributePos.length; i++) {
			jointProbability /= inSum[i];
			jointProbability *= outSum[i];
		}
		// Transfer the tuple iff the joint probability is positive (maybe set quality filter later to reduce the number of tuples)
		if (jointProbability > 0.0) {
			((IProbabilistic) outputVal.getMetadata()).setExistence(jointProbability);
			System.out.println("Transfer-> " + outputVal);
			transfer((T) outputVal);
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
