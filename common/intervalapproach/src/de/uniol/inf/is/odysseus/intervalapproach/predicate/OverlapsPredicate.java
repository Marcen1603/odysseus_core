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
 * Singleton because no object state is needed
 * 
 * @author Jonas Jacobi, Christian Kuka
 */

public class OverlapsPredicate extends AbstractTimeIntervalPredicate {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7697071354543915488L;
    private static final OverlapsPredicate predicate = new OverlapsPredicate();

    public static OverlapsPredicate getInstance() {
        return predicate;
    }

    @Override
	public Boolean evaluate(IStreamObject<? extends ITimeInterval> input) {
        throw new UnsupportedOperationException();
    }

    @Override
	public Boolean evaluate(IStreamObject<? extends ITimeInterval> left, IStreamObject<? extends ITimeInterval> right) {
        return TimeInterval.overlaps(left.getMetadata(), right.getMetadata());
    }

    @Override
    public OverlapsPredicate clone() {
        return this;
    }

    private OverlapsPredicate() {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(IPredicate pred) {
        if (!(pred instanceof OverlapsPredicate)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isContainedIn(IPredicate<?> o) {
        if (!(o instanceof OverlapsPredicate)) {
            return false;
        }
        return true;
    }
}
