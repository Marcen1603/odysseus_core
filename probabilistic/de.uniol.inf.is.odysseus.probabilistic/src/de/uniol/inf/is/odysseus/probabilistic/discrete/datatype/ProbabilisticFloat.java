/*
 * Copyright 2013 The Odysseus Team
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

package de.uniol.inf.is.odysseus.probabilistic.discrete.datatype;

import java.util.Map;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticFloat extends AbstractProbabilisticValue<Float> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9174297465866495711L;

	public ProbabilisticFloat(final Float value, final Double probability) {
		super(value, probability);
	}

	public ProbabilisticFloat(final Map<Float, Double> values) {
		super(values);
	}

	public ProbabilisticFloat(final ProbabilisticFloat other) {
		super(other);
	}

	public ProbabilisticFloat(final Float[] values, final Double[] probabilities) {
		super(values, probabilities);
	}
}
