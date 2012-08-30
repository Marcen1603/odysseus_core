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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.First;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RelationalFirst extends First<Tuple<?>, Tuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9099860331313991458L;
	private static Map<Integer, RelationalFirst> instances = new HashMap<Integer, RelationalFirst>();
	private int pos;

	private RelationalFirst(int pos) {
		super();
		this.pos = pos;
	}

	public static RelationalFirst getInstance(int pos) {
		RelationalFirst ret = instances.get(pos);
		if (ret == null) {
			ret = new RelationalFirst(pos);
		}
		return ret;
	}

	@Override
	public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
		ElementPartialAggregate<Tuple<?>> pa = (ElementPartialAggregate<Tuple<?>>) p;

		@SuppressWarnings("rawtypes")
		Tuple r = new Tuple(1, false);
		r.setAttribute(0, pa.getElem().getAttribute(pos));
		return r;
	}

}
