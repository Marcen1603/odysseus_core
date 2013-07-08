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

package de.uniol.inf.is.odysseus.probabilistic.functions;

import de.uniol.inf.is.odysseus.core.server.mep.IOperator;

public abstract class AbstractProbabilisticUnaryOperator<T> extends AbstractProbabilisticFunction<T> implements IOperator<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5406076763880872083L;

	@Override
	public boolean isBinary() {
		return false;
	}

	@Override
	public boolean isUnary() {
		return true;
	}

	@Override
	public int getArity() {
		return 1;
	}
}
