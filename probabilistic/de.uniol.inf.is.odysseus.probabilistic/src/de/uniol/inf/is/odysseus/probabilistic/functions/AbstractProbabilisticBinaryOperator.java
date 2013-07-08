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

import de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <T>
 */
public abstract class AbstractProbabilisticBinaryOperator<T> extends AbstractProbabilisticFunction<T> implements IBinaryOperator<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2604513567977149416L;

	@Override
	final public int getArity() {
		return 2;
	}

	@Override
	final public boolean isBinary() {
		return true;
	}

	@Override
	final public boolean isUnary() {
		return false;
	}

	@Override
	public boolean isDistributiveWith(final IOperator<T> operator) {
		return this.isLeftDistributiveWith(operator) && this.isRightDistributiveWith(operator);
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer("" + this.getArgument(0));
		buffer.append(" ").append(this.getSymbol()).append(" " + this.getArgument(1));
		return buffer.toString();
	}

}
