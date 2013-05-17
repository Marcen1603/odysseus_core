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
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.NoHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.base.predicate.ProbabilisticPredicate;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;

/**
 * Implementation of a probabilistic Select operator.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <T>
 */
public class ProbabilisticSelectPO<T extends IMetaAttribute> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
	/** Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ProbabilisticSelectPO.class);

	/** The predicate. */
	private final ProbabilisticPredicate predicate;
	/** The heartbeat generation strategy. */
	private IHeartbeatGenerationStrategy<ProbabilisticTuple<T>> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<ProbabilisticTuple<T>>();

	/**
	 * Default constructor.
	 * 
	 * @param predicate The predicate
	 */
	public ProbabilisticSelectPO(final ProbabilisticPredicate predicate) {
		this.predicate = predicate.clone();
	}

	/**
	 * Clone constructor.
	 * 
	 * @param po The copy
	 */
	public ProbabilisticSelectPO(final ProbabilisticSelectPO<T> po) {
		this.predicate = po.predicate.clone();
		this.heartbeatGenerationStrategy = po.heartbeatGenerationStrategy.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe# getOutputMode()
	 */
	@Override
	public final OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe# process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected final void process_next(final ProbabilisticTuple<T> object, final int port) {
		ProbabilisticTuple<T> outputVal = new ProbabilisticTuple<T>(object.getAttributes(), object.requiresDeepClone());
		outputVal.setMetadata((T) object.getMetadata().clone());
		// The MEP function will update the distribution in the meta data. Thus,
		// first create a copy of the object and perform the evaluation on that
		// new object.
		double probability = predicate.probabilisticEvaluate(outputVal);
		((IProbabilistic) outputVal.getMetadata()).setExistence(probability);
		if (probability > 0.0) {
			transfer(outputVal);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe# process_open()
	 */
	@Override
	public final void process_open() {
		this.predicate.init();
	}

	/**
	 * 
	 * @return The predicate
	 */
	public final IPredicate<? super ProbabilisticTuple<T>> getPredicate() {
		return this.predicate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#clone ()
	 */
	@Override
	public final ProbabilisticSelectPO<T> clone() {
		return new ProbabilisticSelectPO<T>(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#toString ()
	 */
	@Override
	public final String toString() {
		return super.toString() + " predicate: " + this.getPredicate().toString();
	}

	/**
	 * Gets the heartbeat generation strategy.
	 * 
	 * @return The heartbeat generation strategy
	 */
	public final IHeartbeatGenerationStrategy<ProbabilisticTuple<T>> getHeartbeatGenerationStrategy() {
		return this.heartbeatGenerationStrategy;
	}

	/**
	 * Sets the heartbeat generation strategy.
	 * 
	 * @param heartbeatGenerationStrategy
	 *            The heartbeat generation strategy
	 */
	public final void setHeartbeatGenerationStrategy(final IHeartbeatGenerationStrategy<ProbabilisticTuple<T>> heartbeatGenerationStrategy) {
		this.heartbeatGenerationStrategy = heartbeatGenerationStrategy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource# process_isSemanticallyEqual (de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator)
	 */
	@Override
	public final boolean process_isSemanticallyEqual(final IPhysicalOperator ipo) {
		if (!(ipo instanceof ProbabilisticSelectPO<?>)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		final ProbabilisticSelectPO<T> spo = (ProbabilisticSelectPO<T>) ipo;
		// Different sources
		if (!this.hasSameSources(spo)) {
			return false;
		}
		// Predicates match
		if (this.predicate.equals(spo.getPredicate()) || (this.predicate.isContainedIn(spo.getPredicate()) && spo.getPredicate().isContainedIn(this.predicate))) {
			return true;
		}

		return false;
	}

}
