/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
abstract public class Nth<R, W> extends AbstractAggregateFunction<R, W> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3840479318065114625L;
	private int n;

    protected Nth(int n, boolean partialAggregateInput) {
        super("NTH", partialAggregateInput);
        this.n = n;
    }

    @Override
    public IPartialAggregate<R> init(R in) {
        IPartialAggregate<R> pa = new NthPartialAggregate<R>(in);
        return pa;
    }

    @Override
    public IPartialAggregate<R> merge(IPartialAggregate<R> p, R toMerge, boolean createNew) {
        NthPartialAggregate<R> pa = null;
        if (createNew) {
            pa = new NthPartialAggregate<R>(((NthPartialAggregate<R>) p).getElem());
        }
        else {
            pa = (NthPartialAggregate<R>) p;
        }
        if (pa.getN() != n) {
            pa.addElem(toMerge);
        }
        return pa;
    }

    protected int getN() {
        return n;
    }
}
