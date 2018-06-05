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
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.expression.AbstractRelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.UpdatePredicatePunctuation;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPipe;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class SelectPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> implements IHasPredicate {

	private IPredicate<? super T> predicate;
	private int heartbeatRate;
	private IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<>();

	private boolean predicateIsUpdateable;

	public SelectPO(IPredicate<? super T> predicate) {
		this.predicate = predicate.clone();
		this.predicateIsUpdateable = false;
	}

	public SelectPO(boolean predicateIsUpdateable,
			IPredicate<? super T> predicate) {
		this.predicate = predicate.clone();
		this.predicateIsUpdateable = predicateIsUpdateable;
	}

	@Override
	public IPredicate<? super T> getPredicate() {
		return predicate;
	}

	/**
	 * @return the heartbeat rate
	 */
	public int getHeartbeatRate() {
		return this.heartbeatRate;
	}

	/**
	 * Set the heartbeat rate
	 *
	 * @param heartbeatRate
	 */
	public void setHeartbeatRate(int heartbeatRate) {
		this.heartbeatRate = heartbeatRate;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public boolean deliversStoredElement(int outputPort) {
		return false;
	}

	@Override
	protected void process_next(T object, int port) {
		try {
			if (predicate.evaluate(object)) {
				transfer(object);
			} else {
				// Send filtered data to output port 1
				// Removed sending negated elements to port 1 --> use Route
				// instead (Selectivity measurement will always be one in this
				// case)
				// transfer(object,1);
				heartbeatGenerationStrategy.generateHeartbeat(object, this);
			}
		} catch (Exception e) {
			infoService.warning("Cannot evaluate " + predicate + " predicate with input " + object, e);
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		/*
		 * (1) Expressions can be updated with punctuations, (2) this is a
		 * punctuation to update the expressions and (3) this operator is in the
		 * list with the targets for this punctuation
		 */
		if (this.predicateIsUpdateable && punctuation instanceof UpdatePredicatePunctuation
				&& ((UpdatePredicatePunctuation) punctuation).getTargetOperatorNames().contains(this.getName())) {
			UpdatePredicatePunctuation updatePredicatePunctuation = (UpdatePredicatePunctuation) punctuation;
			this.updatePredicate(updatePredicatePunctuation);
		}

		IPunctuation puncToSend = predicate.processPunctuation(punctuation);
		sendPunctuation(puncToSend);
	}

	/**
	 * Updates the predicate of the operator via a punctuation
	 * 
	 * @param updatePredicatePunctuation
	 *            the punctuation which has the new predicate
	 */
	private void updatePredicate(UpdatePredicatePunctuation updatePredicatePunctuation) {
		IPredicate<?> newPredicate = updatePredicatePunctuation.getNewPredicate();

		if (newPredicate instanceof AbstractRelationalExpression) {
			@SuppressWarnings("unchecked")
			AbstractRelationalExpression<? super T> newExpression = (AbstractRelationalExpression<? super T>) newPredicate;

			// Re-initialize, cause the original expression was created
			// without knowing the actual schema
			newExpression.initVars(getOutputSchema());
			this.setPredicate(newExpression);
		} else {
			// Maybe it's not relational but keyValue. In this case, the
			// expressions does not need to be re-initialized.
			this.setPredicate(newPredicate);
		}
	}

	@Override
	public void process_open() throws OpenFailedException {
	}

	@Override
	public String toString() {
		return super.toString() + " predicate: " + this.getPredicate().toString();
	}

	public IHeartbeatGenerationStrategy<T> getHeartbeatGenerationStrategy() {
		return heartbeatGenerationStrategy;
	}

	public void setHeartbeatGenerationStrategy(IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy) {
		this.heartbeatGenerationStrategy = heartbeatGenerationStrategy;
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof SelectPO<?>)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		SelectPO<T> spo = (SelectPO<T>) ipo;
		// Predicates match
		if (this.predicate.equals(spo.getPredicate()) || (this.predicate.isContainedIn(spo.getPredicate())
				&& spo.getPredicate().isContainedIn(this.predicate))) {
			return true;
		}

		return false;
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public boolean isContainedIn(IPipe<T, T> ip) {
		if (!(ip instanceof SelectPO)) {
			return false;
		}
		// Sonderfall, dass das Pr�dikat des anderen SelectPOs ein OrPredicate
		// ist und das Pr�dikat von diesem SelectPO nicht.
		if ((ComplexPredicateHelper.isOrPredicate(((SelectPO) ip).getPredicate())
				&& !ComplexPredicateHelper.isOrPredicate(this.predicate))) {

			return ((OrPredicate) ((SelectPO) ip).getPredicate()).contains(this.predicate);
		}
		if (this.predicate.isContainedIn(((SelectPO<T>) ip).predicate)) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setPredicate(IPredicate<?> predicate) {
		this.predicate = (IPredicate<? super T>) predicate;
	}

}
