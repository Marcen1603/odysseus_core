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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public abstract class AbstractBinaryBitVectorInputOperator<T> extends AbstractBinaryOperator<T>{
	
	private static final long serialVersionUID = -195306610974958000L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][]{{SDFDatatype.BITVECTOR},{SDFDatatype.BITVECTOR}};
	
	public AbstractBinaryBitVectorInputOperator(String symbol, SDFDatatype returnType) {
		super(symbol,accTypes, returnType);
	}
	
	@Override
	public boolean isCommutative() {
		return false;
	}

	@Override
	public boolean isAssociative() {
		return false;
	}

	@Override
	public boolean isLeftDistributiveWith(IOperator<T> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<T> operator) {
		return false;
	}
	
	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return null;
	}
	
}
