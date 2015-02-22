/*
 * Copyright 2015 Marcus Behrendt
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
 *
**/

package de.uniol.inf.is.odysseus.trajectory.logicaloperator.builder;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;

/**
 * Specialization of <tt>AggregateItem</tt> which only allows <i>NEST</i> aggregations.
 * 
 * @author marcus
 *
 */
public class NestAggregateItem extends AggregateItem {

	public NestAggregateItem(final List<SDFAttribute> attributes, final SDFAttribute outAttr) {
		super("NEST", attributes, outAttr);
	}

	public NestAggregateItem(final SDFAttribute attribute,
			final SDFAttribute outAttr) {
		super("NEST", attribute, outAttr);
	}	
}
