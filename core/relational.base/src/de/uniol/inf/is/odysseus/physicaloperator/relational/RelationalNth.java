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
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.Nth;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.NthPartialAggregate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RelationalNth extends Nth<Tuple<?>, Tuple<?>> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8236978756283300379L;
	private final static Map<Integer, RelationalNth> instances = new HashMap<Integer, RelationalNth>();

    static public RelationalNth getInstance(Integer n) {
        RelationalNth instance = instances.get(n);
        if (instance == null) {
            instance = new RelationalNth(n);
            instances.put(n, instance);
        }
        return instance;
    }

    private RelationalNth(int n) {
        super(n);
    }

    @Override
    public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
        NthPartialAggregate<Tuple<?>> pa = (NthPartialAggregate<Tuple<?>>) p;
        if (pa.getN() == this.getN()) {
            return pa.getElem();
        }
        return null;
    }
}
