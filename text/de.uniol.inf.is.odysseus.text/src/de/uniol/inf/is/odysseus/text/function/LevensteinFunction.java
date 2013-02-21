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
package de.uniol.inf.is.odysseus.text.function;

import org.apache.commons.lang3.StringUtils;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/**
 * MEP function to compute the levenstein distance of a string
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class LevensteinFunction extends AbstractFunction<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5254931226986934896L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.STRING }, { SDFDatatype.STRING } };

	@Override
	public int getArity() {
		return 2;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument(s): two strings");
		}
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "levenstein";
	}

	@Override
	public Integer getValue() {
		return StringUtils.getLevenshteinDistance(getInputValue(0).toString(),
				getInputValue(1).toString());
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.INTEGER;
	}

}
