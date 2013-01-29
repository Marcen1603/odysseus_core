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

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.NoHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <T>
 */
public class ProbabilisticSelectPO<T extends IStreamObject<?>> extends
		AbstractPipe<T, T> {

	private final IPredicate<? super T> predicate;
	private IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<T>();

	public ProbabilisticSelectPO(final IPredicate<? super T> predicate) {
		this.predicate = predicate.clone();
	}

	public ProbabilisticSelectPO(final ProbabilisticSelectPO<T> po) {
		this.predicate = po.predicate.clone();
		this.heartbeatGenerationStrategy = po.heartbeatGenerationStrategy
				.clone();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(final T object, final int port) {
		predicate.getAttributes();
		predicate.evaluate(object);
		// ((IProbabilistic) object.getMetadata()).setExistence(prob);
		transfer(object);
	}

	@Override
	public void process_open() throws OpenFailedException {
		this.predicate.init();
	}

	public IPredicate<? super T> getPredicate() {
		return this.predicate;
	}

	@Override
	public ProbabilisticSelectPO<T> clone() {
		return new ProbabilisticSelectPO<T>(this);
	}

	@Override
	public String toString() {
		return super.toString() + " predicate: "
				+ this.getPredicate().toString();
	}

	public IHeartbeatGenerationStrategy<T> getHeartbeatGenerationStrategy() {
		return this.heartbeatGenerationStrategy;
	}

	public void setHeartbeatGenerationStrategy(
			final IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy) {
		this.heartbeatGenerationStrategy = heartbeatGenerationStrategy;
	}

	@Override
	public boolean process_isSemanticallyEqual(final IPhysicalOperator ipo) {
		if (!(ipo instanceof SelectPO<?>)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		final ProbabilisticSelectPO<T> spo = (ProbabilisticSelectPO<T>) ipo;
		// Different sources
		if (!this.hasSameSources(spo)) {
			return false;
		}
		// Predicates match
		if (this.predicate.equals(spo.getPredicate())
				|| (this.predicate.isContainedIn(spo.getPredicate()) && spo
						.getPredicate().isContainedIn(this.predicate))) {
			return true;
		}

		return false;
	}

}
