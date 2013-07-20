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
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public abstract class AbstractProbabilisticBinaryOperator<T> extends AbstractProbabilisticFunction<T> implements IBinaryOperator<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2604513567977149416L;

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getArity()
	 */
	@Override
	public final int getArity() {
		return 2;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IOperator#isBinary()
	 */
	@Override
	public final boolean isBinary() {
		return true;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IOperator#isUnary()
	 */
	@Override
	public final boolean isUnary() {
		return false;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#isDistributiveWith(de.uniol.inf.is.odysseus.core.server.mep.IOperator)
	 */
	@Override
	public final boolean isDistributiveWith(final IOperator<T> operator) {
		return this.isLeftDistributiveWith(operator) && this.isRightDistributiveWith(operator);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction#toString()
	 */
	@Override
	public final String toString() {
		final StringBuffer buffer = new StringBuffer("" + this.getArgument(0));
		buffer.append(" ").append(this.getSymbol()).append(" " + this.getArgument(1));
		return buffer.toString();
	}

}
