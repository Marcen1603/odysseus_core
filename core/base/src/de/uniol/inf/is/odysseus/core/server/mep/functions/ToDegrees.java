/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ToDegrees extends AbstractFunction<Double> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3137250935924025881L;
	private static final SDFDatatype[] accTypes = new SDFDatatype[] {
			SDFDatatype.DOUBLE, SDFDatatype.BYTE, SDFDatatype.FLOAT,
			SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.STRING };

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > 0) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument(s).");
		}
        return accTypes;
	}

	@Override
	public String getSymbol() {
		return "ToDegrees";
	}

	@Override
	public Double getValue() {
		Double val = Double.parseDouble(getInputValue(0).toString());
		return Math.toDegrees(val);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}

}
