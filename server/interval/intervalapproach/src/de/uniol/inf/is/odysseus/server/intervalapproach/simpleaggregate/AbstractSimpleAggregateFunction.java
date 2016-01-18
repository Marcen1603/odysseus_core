/**
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
package de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.functions.SimpleAggregateFunction;

/**
 * @author Cornelius Ludmann
 *
 */
public abstract class AbstractSimpleAggregateFunction<M extends ITimeInterval, T extends Tuple<M>>
		implements SimpleAggregateFunction<M, T> {

	protected final int[] attributes;
	protected final boolean allAttributes;
	protected int outputAttribute;

	public AbstractSimpleAggregateFunction(int[] attributes) {
		this.attributes = attributes;
		this.allAttributes = false;
	}

	public AbstractSimpleAggregateFunction() {
		this.attributes = new int[0];
		this.allAttributes = true;
	}

	protected Object[] getAttributes(T element) {
		if (isAllAttributes()) {
			return element.getAttributes();
		} else if(isSingleAttribute()){
			Object[] r = new Object[1];
			r[0] = element.getAttribute(attributes[0]);
			return r;
		} else {
			return element.restrict(attributes, true).getAttributes();
		}
	}
	
	@SuppressWarnings("unchecked")
	protected T getAttributesAsTuple(T element) {
		if (isAllAttributes()) {
			return element;
		} else {
			return (T) element.restrict(attributes, true);
		}
	}

	protected boolean isSingleAttribute() {
		return attributes.length == 1;
	}

	protected boolean isAllAttributes() {
		return allAttributes;
	}

	/**
	 * @return the outputAttribute
	 */
	public int getOutputAttribute() {
		return outputAttribute;
	}

	/**
	 * @param outputAttribute
	 *            the outputAttribute to set
	 */
	public void setOutputAttribute(int outputAttribute) {
		this.outputAttribute = outputAttribute;
	}

}
