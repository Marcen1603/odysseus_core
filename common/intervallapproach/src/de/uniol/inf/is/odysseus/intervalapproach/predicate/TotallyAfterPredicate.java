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
package de.uniol.inf.is.odysseus.intervalapproach.predicate;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * Singleton because no object state is needed.
 * 
 * This will also be needed parallel to TotallyBeforePredicate, because it is
 * impossible to switch between Order.LeftRight and Order.RightLeft in complex
 * predicates.
 * 
 * @author Andre Bolles, Christian Kuka
 */
public class TotallyAfterPredicate extends AbstractTimeIntervalPredicate {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1432746295344925590L;
    private static final TotallyAfterPredicate predicate = new TotallyAfterPredicate();

    @Override
	public Boolean evaluate(IStreamObject<? extends ITimeInterval> input) {
        throw new UnsupportedOperationException();
    }

    @Override
	public Boolean evaluate(IStreamObject<? extends ITimeInterval> left, IStreamObject<? extends ITimeInterval> right) {
        return TimeInterval.totallyAfter(left.getMetadata(), right.getMetadata());
    }

    @Override
    public TotallyAfterPredicate clone() {
        return this;
    }

    public static TotallyAfterPredicate getInstance() {
        return predicate;
    }

    private TotallyAfterPredicate() {

    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(IPredicate pred) {
        return (pred instanceof TotallyAfterPredicate);
    }

    @Override
    public boolean isContainedIn(IPredicate<?> o) {
        if (!(o instanceof TotallyAfterPredicate)) {
            return false;
        }
        return true;
    }

}
