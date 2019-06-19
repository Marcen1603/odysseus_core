/********************************************************************************** 
 * Copyright 2016 The Odysseus Team
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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Michael Brand <michael.brand@uol.de>
 *
 */
public class AsByteList extends AbstractFunction<List<Byte>> {

	private static final long serialVersionUID = -1735081230552487386L;
	
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
			{ SDFDatatype.LIST_BYTE, SDFDatatype.OBJECT } };

	public AsByteList() {
		super("asByteList", 1, ACC_TYPES, SDFDatatype.LIST_BYTE);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Byte> getValue() {
		return (List<Byte>) getInputValue(0);
	}

}