/*******************************************************************************
 * Copyright 2016 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.functions.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractUnaryStringObjectInputFunction;

/**
 * Converts a {@link SDFDatatype#STRING} into a {@link SDFDatatype#LIST_BYTE}.
 * 
 * @author Michael Brand <michael.brand@uol.de>
 */
public class ToByteListFromStringFunction extends AbstractUnaryStringObjectInputFunction<List<Byte>> {

	private static final long serialVersionUID = -7341145590147300647L;

	public ToByteListFromStringFunction() {
		super("toByteList", SDFDatatype.LIST_BYTE);
	}

	@Override
	public List<Byte> getValue() {
		byte[] primBytes = getInputValue(0).toString().getBytes();
		List<Byte> bytes = new ArrayList<>(primBytes.length);
		for (byte b : primBytes) {
			bytes.add(b);
		}
		return bytes;
	}
}
