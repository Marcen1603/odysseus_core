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
package de.uniol.inf.is.odysseus.mep.functions;

import java.util.Random;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class RandomFunction2 extends AbstractFunction<Integer> {

	private static final SDFDatatype[] accTypes1 = new SDFDatatype[] { SDFDatatype.BYTE,  SDFDatatype.INTEGER, SDFDatatype.LONG };
	private static final SDFDatatype[] accTypes2 = new SDFDatatype[] { SDFDatatype.INTEGER};

	/**
	 * 
	 */
	private static final long serialVersionUID = -5923905039135136079L;

	private Random rnd;

	@Override
	public int getArity() {
		return 2;
	}

	@Override
	public String getSymbol() {
		return "random";
	}

	@Override
	public Integer getValue() {
		if (rnd == null){
			rnd = new Random(getNumericalInputValue(0).longValue());
		}
		return rnd.nextInt(getNumericalInputValue(1).intValue());
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.INTEGER;
	}
	
	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos){
		if (argPos == 0){	
			return accTypes1;
		}else if (argPos == 1){
			return accTypes2;
		}
		return null;
	}
	
	@Override
	public boolean optimizeConstantParameter() {
		return false;
	}

}
