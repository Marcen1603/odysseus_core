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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Function to transform an input to a list of bytes. The input can either be a
 * string or a list of integers or bytes.
 * 
 * @author Michael Brand <michael.brand@uol.de>
 *
 */
public class ToByteList extends AbstractFunction<List<Byte>> {

	private static final long serialVersionUID = -1735081230552487386L;

	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
			{ SDFDatatype.LIST_BYTE, SDFDatatype.STRING, SDFDatatype.OBJECT } };

	public ToByteList() {
		super("toByteList", 1, ACC_TYPES, SDFDatatype.LIST_BYTE);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Byte> getValue() {
		Object input = getInputValue(0);
		if (input == null) {
			return null;
		} else if (input instanceof String) {
			byte[] bytes = ((String) input).getBytes();
			List<Byte> retVal = new ArrayList<>(bytes.length);
			for (byte b : bytes) {
				retVal.add(b);
			}
			return retVal;
		} else if (!(input instanceof List)) {
			throw new IllegalArgumentException("Input of toByteList must be a list (Integer or Byte) or String!");
		}

		List<?> list = (List<?>) input;
		if (list.isEmpty()) {
			return new ArrayList<>();
		} else if (list.get(0) instanceof Byte) {
			return (List<Byte>) list;
		} else if (list.get(0) instanceof Integer) {
			List<Byte> retVal = new ArrayList<>(list.size());
			for (Integer i : (List<Integer>) list) {
				retVal.add(i.byteValue());
			}
			return retVal;
		} else {
			throw new IllegalArgumentException("Input of toByteList must be a list (Integer or Byte) or String!");
		}
	}

}