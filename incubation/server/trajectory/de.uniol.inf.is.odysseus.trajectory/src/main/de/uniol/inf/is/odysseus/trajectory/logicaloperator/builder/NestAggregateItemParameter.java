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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;

/**
 * 
 * Class for resolving <tt>NestAggregateItem</tt>.
 * 
 * @author marcus
 *
 */
public class NestAggregateItemParameter extends AbstractParameter<NestAggregateItem> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3197742185184362004L;

	public NestAggregateItemParameter() {
		super();
	}

	public NestAggregateItemParameter(final String name, final REQUIREMENT requirement, final USAGE usage,
			final String doc) {
		super(name, requirement, usage, doc);
	}

	public NestAggregateItemParameter(final String name, final REQUIREMENT requirement, final USAGE usage) {
		super(name, requirement, usage);
	}

	public NestAggregateItemParameter(final String name, final REQUIREMENT requirement) {
		super(name, requirement, USAGE.RECENT);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalAssignment() {
		final List<String> value = (List<String>) this.inputValue;
		if(value.size() != 2) {
			throw new IllegalParameterException("illegal value for aggregation");
		}
		this.setValue(
				new NestAggregateItem(
						this.getAttributeResolver().getAttribute(value.get(0)), 
						new SDFAttribute(null, value.get(1), this.getDataDictionary().getDatatype("double"), null, null, null)
				)
		);
	}

	@Override
	protected String getPQLStringInternal() {
		return null;
	}

}
