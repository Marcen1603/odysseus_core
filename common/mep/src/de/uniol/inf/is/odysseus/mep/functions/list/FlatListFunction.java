/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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

import java.util.List;
import java.util.stream.Collectors;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class FlatListFunction extends AbstractFunction<List<?>> {

	private static final long serialVersionUID = -7523471366971911783L;

	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.getLists() };

	public FlatListFunction() {
		super("flat", 1, ACC_TYPES, SDFDatatype.LIST, false);
	}

	@Override
	public List<?> getValue() {
		@SuppressWarnings("unchecked")
		List<List<?>> list = (List<List<?>>) getInputValue(0);
		return list.stream().filter(elem -> elem != null).flatMap(List::stream).collect(Collectors.toList());
	}

}
