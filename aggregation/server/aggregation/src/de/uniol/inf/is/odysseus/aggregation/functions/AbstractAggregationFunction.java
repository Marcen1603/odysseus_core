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
package de.uniol.inf.is.odysseus.aggregation.functions;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

/**
 * @author Cornelius Ludmann
 *
 */
public abstract class AbstractAggregationFunction<M extends ITimeInterval, T extends Tuple<M>>
		implements IAggregationFunction {

	private static final long serialVersionUID = -5488917321263100233L;

	/**
	 * The indices of the input attributes that are used by this function. If
	 * all input attributes in original order should be used,
	 * inputAttributeIndices should be null.
	 */
	protected final int[] inputAttributeIndices;

	/**
	 * True, iff this function needs all incoming attributes.
	 */
	protected final boolean allAttributes;

	/**
	 * The indices of the attributes of the outgoing tuple where the result of
	 * this function should be stored.
	 */
	protected int[] outputAttributeIndices = new int[0];

	protected final String[] outputAttributeNames;

	/**
	 * 
	 */
	public AbstractAggregationFunction() {
		this.inputAttributeIndices = new int[0];
		this.allAttributes = true;
		this.outputAttributeNames = new String[0];
	}

	public AbstractAggregationFunction(final int[] attributes, final String[] outputAttributeNames) {
		if (attributes != null) {
			this.inputAttributeIndices = Arrays.copyOf(attributes, attributes.length);
			this.allAttributes = false;
		} else {
			this.inputAttributeIndices = null;
			this.allAttributes = true;
		}
		this.outputAttributeNames = Arrays.copyOf(outputAttributeNames, outputAttributeNames.length);
	}

	public AbstractAggregationFunction(final int[] attributes, final boolean needsAllAttributes,
			final String[] outputAttributeNames) {
		if (attributes != null) {
			this.inputAttributeIndices = Arrays.copyOf(attributes, attributes.length);
		} else {
			this.inputAttributeIndices = null;
		}
		this.allAttributes = needsAllAttributes;
		this.outputAttributeNames = Arrays.copyOf(outputAttributeNames, outputAttributeNames.length);
	}

	public AbstractAggregationFunction(final AbstractAggregationFunction<M, T> other) {
		// Save copies?
		this.inputAttributeIndices = other.inputAttributeIndices;
		this.allAttributes = other.allAttributes;
		this.outputAttributeIndices = other.outputAttributeIndices;
		this.outputAttributeNames = other.outputAttributeNames;
	}

	protected <E> E getFirstAttribute(final T element) {
		if (inputAttributeIndices == null) {
			return element.getAttribute(0);
		}
		return element.getAttribute(inputAttributeIndices[0]);
	}

	protected Object[] getAttributes(final T element) {
		if (inputAttributeIndices == null) {
			// save copy?
			return element.getAttributes();
		} else if (isSingleAttribute()) {
			final Object[] r = new Object[1];
			r[0] = element.getAttribute(inputAttributeIndices[0]);
			return r;
		} else {
			return element.restrict(inputAttributeIndices, true).getAttributes();
		}
	}

	@SuppressWarnings("unchecked")
	protected T getAttributesAsTuple(final T element) {
		if (inputAttributeIndices == null) {
			return (T) element.clone();
		} else {
			return (T) element.restrict(inputAttributeIndices, true);
		}
	}

	protected boolean isSingleAttribute() {
		return inputAttributeIndices.length == 1;
	}

	@Override
	public boolean needsAllAttributes() {
		return allAttributes;
	}

	/**
	 * @return the outputAttribute
	 */
	@Override
	public int[] getOutputAttributeIndices() {
		return outputAttributeIndices;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction#
	 * setOutputAttributeIndices(int[])
	 */
	@Override
	public void setOutputAttributeIndices(final int[] attr) {
		this.outputAttributeIndices = attr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction#
	 * getName()
	 */
	@Override
	public String getFunctionName() {
		return this.getClass().getSimpleName();
	}
}
