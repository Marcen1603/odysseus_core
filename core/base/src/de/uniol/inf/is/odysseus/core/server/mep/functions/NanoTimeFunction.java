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
package de.uniol.inf.is.odysseus.core.server.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/**
 * Returns the current value of the time source, in nanoseconds.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class NanoTimeFunction extends AbstractFunction<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1135046519531015301L;
	private static final SDFDatatype[] accTypes = new SDFDatatype[] {};

	@Override
	public int getArity() {
		return 0;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has "
					+ this.getArity() + " argument(s).");
		}
		return accTypes;
	}

	@Override
	public String getSymbol() {
		return "nanoTime";
	}

	@Override
	public Long getValue() {
		return System.nanoTime();
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.LONG;
	}

}
