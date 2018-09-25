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
public class SplitStringToListFunction extends AbstractFunction<List<?>> {

	private static final long serialVersionUID = -2373175510309535913L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.STRING },
			{ SDFDatatype.STRING } };

	public SplitStringToListFunction() {
		super("split", 2, accTypes, SDFDatatype.LIST);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
	 */
	@Override
	public List<?> getValue() {
		String value = getInputValue(0);
		String delim = getInputValue(1);
		String[] v = value.split(delim);
		if (v != null && v.length > 0) {
			if (v.length == 1 && v[0].trim().equals("")) {
				return Collections.emptyList();
			}
			return Arrays.asList(v);
		} else {
			return Collections.emptyList();
		}
	}
}
