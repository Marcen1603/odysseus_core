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

public abstract class AbstractUnaryStringInputFunction<T> extends AbstractFunction<T> {

	private static final long serialVersionUID = 4163516033012192570L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { {SDFDatatype.STRING} };
	
	public AbstractUnaryStringInputFunction(String symbol, SDFDatatype returnType) {
		super(symbol,1,accTypes, returnType);
	}

	
}
