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
package de.uniol.inf.is.odysseus.mep.functions.bool;

import de.uniol.inf.is.odysseus.mep.IOperator;

public class OrOperator extends AbstractBinaryBooleanOperator{

	private static final long serialVersionUID = 5508300237695874743L;

	public OrOperator() {
		super("||");
	}
	
	@Override
	public boolean isCommutative() {
		return true;
	}

	@Override
	public boolean isAssociative() {
		return true;
	}

	@Override
	public boolean isLeftDistributiveWith(IOperator<Boolean> operator) {
		return operator.getClass() == AndOperator.class;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Boolean> operator) {
		return operator.getClass() == AndOperator.class;
	}

	@Override
    public int getPrecedence() {
        return 15;
    }

	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	@Override
	public Boolean getValue() {
		Boolean left = getInputValue(0);
		if (left != null && left == true) {
			return Boolean.TRUE;
		}
		Boolean right = getInputValue(1);
		if ((left == null) || (right == null)) {
			if (((left == null) && (right == null)) || ((left == null) && (right != null) && (right == false))
					|| ((right == null) && (left != null) && (left == false))) {
				return null;
			}
			return Boolean.TRUE;
		}
		return Boolean.valueOf(left || right);
	}

}
