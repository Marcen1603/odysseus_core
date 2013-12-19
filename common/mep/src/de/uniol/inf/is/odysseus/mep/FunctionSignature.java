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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class FunctionSignature {
	private final String symbol;
	private final List<SDFDatatype[]> parameters;

	public FunctionSignature(String symbol, List<SDFDatatype[]> parameters) {
		this.symbol = symbol.toUpperCase();
		if (parameters == null) {
			this.parameters = new ArrayList<SDFDatatype[]>();
		} else {
			this.parameters = parameters;
		}
	}

	public FunctionSignature(FunctionSignature functionSignature) {
		this.symbol = functionSignature.symbol;
		this.parameters = new ArrayList<SDFDatatype[]>(
				functionSignature.parameters);
	}

	public String getSymbol() {
		return symbol;
	}

	public boolean contains(List<SDFDatatype> parameters) {
		boolean contains = true;
        if (parameters.size() != this.parameters.size()) {
            return false;
        }
		for (int i = 0; i < parameters.size(); i++) {
			SDFDatatype[] datatypes = this.parameters.get(i);
			if (!Arrays.asList(datatypes).contains(parameters.get(i))) {
				contains = false;
				break;
			}
		}
		return contains;
	}

	@Override
	public FunctionSignature clone() {
		return new FunctionSignature(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FunctionSignature other = (FunctionSignature) obj;
		if (parameters == null) {
			if (other.parameters != null) {
				return false;
			}
		} else if (!parameters.equals(other.parameters)) {
			return false;
		}
		if (symbol == null) {
			if (other.symbol != null) {
				return false;
			}
		} else if (!symbol.equals(other.symbol)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(symbol).append("(");
		int i = 0;
		for (SDFDatatype[] parameter : parameters) {
			int j = 0;
			if (i > 0) {
				sb.append(",");
			}
			for (SDFDatatype type : parameter) {
				if (j > 0) {
					sb.append("|");
				}
				sb.append(type);
				j++;
			}
			i++;
		}
		sb.append(")");

		return sb.toString();
	}

}
