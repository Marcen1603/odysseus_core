/********************************************************************************** 
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
package de.uniol.inf.is.odysseus.intervalapproach.predicate;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.core.predicate.OrPredicate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public abstract class AbstractTimeIntervalPredicate extends AbstractPredicate<IStreamObject<? extends ITimeInterval>> {

    /**
     * 
     */
    private static final long serialVersionUID = -2491233125954552148L;

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<IStreamObject<? extends ITimeInterval>> and(IPredicate<IStreamObject<? extends ITimeInterval>> predicate) {
        return new AndPredicate<>(this, predicate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<IStreamObject<? extends ITimeInterval>> or(IPredicate<IStreamObject<? extends ITimeInterval>> predicate) {
        return new OrPredicate<>(this, predicate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<IStreamObject<? extends ITimeInterval>> not() {
        return new NotPredicate<>(this);
    }
}
