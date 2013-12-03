/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep;

public abstract class AbstractBinaryOperator<T> extends AbstractFunction<T>
		implements IBinaryOperator<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8717397809265227223L;

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
	public boolean isDistributiveWith(IOperator<T> operator) {
		return isLeftDistributiveWith(operator)
				&& isRightDistributiveWith(operator);
	}
	
	@Override
	protected String _internalToString() {
		StringBuffer buffer = new StringBuffer(""+getArgument(0));
		buffer.append(" ").append(getSymbol()).append(" "+getArgument(1));
		return buffer.toString();
	}
}
