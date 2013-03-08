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

package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.util.Map;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticShort extends AbstractProbabilisticValue<Short> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -969773405115701795L;

	public ProbabilisticShort(final Short value, Double probability) {
		super(value, probability);
	}

	public ProbabilisticShort(final Map<Short, Double> values) {
		super(values);
	}

	public ProbabilisticShort(ProbabilisticShort other) {
		super(other);
	}

	public ProbabilisticShort(final Short[] values, final Double[] probabilities) {
		super(values, probabilities);
	}
}
