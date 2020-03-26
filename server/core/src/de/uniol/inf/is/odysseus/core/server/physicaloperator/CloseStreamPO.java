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

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPipe;

/**
 * @author Marco Grawunder
 */
public class CloseStreamPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> implements IHasPredicate {

	private IPredicate<? super T> predicate;

	private long count = 0;
	private long maxCount = 0;

	public CloseStreamPO(IPredicate<? super T> predicate) {
		if (predicate != null){
			this.predicate = predicate.clone();
		}
	}

	@Override
	public IPredicate<? super T> getPredicate() {
		return predicate;
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
		if (!isDone()) {
			try {
				if (predicate != null && predicate.evaluate(object)) {
					propagateDone();
				} else {
					count++;
					transfer(object);
					
					/// for use of counting, the close condition may be reached now
					if(maxCount > 0 && count >= maxCount) {
						propagateDone();
					}
				}
			} catch (Exception e) {
				infoService.warning("Cannot evaluate " + predicate + " predicate with input " + object, e);
			}
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		IPunctuation puncToSend;
		if(predicate != null) {
			puncToSend = predicate.processPunctuation(punctuation);
		} else {
			puncToSend = punctuation;
		}
		sendPunctuation(puncToSend);
	}

	@Override
	public void process_open() throws OpenFailedException {
		count = 0;
	}

	public void setMaxCount(long maxCount) {
		this.maxCount = maxCount;
	}

	@Override
	public String toString() {
		return super.toString() + " predicate: " + this.getPredicate()+ " count="+maxCount;
	}


	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof CloseStreamPO<?>)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		CloseStreamPO<T> spo = (CloseStreamPO<T>) ipo;
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
		if (!(ip instanceof CloseStreamPO)) {
			return false;
		}
		// Sonderfall, dass das Pr�dikat des anderen SelectPOs ein OrPredicate
		// ist und das Pr�dikat von diesem SelectPO nicht.
		if ((ComplexPredicateHelper.isOrPredicate(((CloseStreamPO) ip).getPredicate())
				&& !ComplexPredicateHelper.isOrPredicate(this.predicate))) {

			return ((OrPredicate) ((CloseStreamPO) ip).getPredicate()).contains(this.predicate);
		}
		if (this.predicate.isContainedIn(((CloseStreamPO<T>) ip).predicate)) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setPredicate(IPredicate<?> predicate) {
		this.predicate = (IPredicate<? super T>) predicate.clone();
	}



}
