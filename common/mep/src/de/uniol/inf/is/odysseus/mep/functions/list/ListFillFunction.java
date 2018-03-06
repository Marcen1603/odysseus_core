/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Cornelius Ludmann
 *
 */
public class ListFillFunction extends AbstractFunction<List<?>> {
	private static final long serialVersionUID = -3114684384693799438L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.getLists(),
			SDFDatatype.DISCRETE_NUMBERS, { SDFDatatype.OBJECT } };

	public ListFillFunction() {
		super("fill", 3, accTypes, SDFDatatype.LIST);
	}

	@Override
	public List<?> getValue() {
		List<?> in = getInputValue(0);
		if(in == null) {
			in = Collections.emptyList();
		}
		int size = this.<Number> getInputValue(1).intValue();
		if (in.size() < size) {
			Object obj = getInputValue(2);
			Object[] out = new Object[size];
			out = in.toArray(out);
			for (int i = in.size(); i < out.length; ++i) {
				out[i] = obj;
			}
			return Arrays.asList(out);
		}
		return in;
	}
}
