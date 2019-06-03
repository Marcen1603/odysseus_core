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
package de.uniol.inf.is.odysseus.mep.functions.transform;

import java.io.UnsupportedEncodingException;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Input parameters byte list and charset (default utf-8), output string.
 * 
 * @author Michael Brand <michael.brand@uol.de>
 *
 */
public class ToStringFromByteListCharsetFunction extends AbstractFunction<String> {

	private static final long serialVersionUID = -2939309507071187982L;
	
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFDatatype.LIST_BYTE }, { SDFDatatype.STRING } };

	protected ToStringFromByteListCharsetFunction(int arity, SDFDatatype[][] acctypes) {
		super("toStringFromByteList", arity, acctypes, SDFDatatype.STRING);
	}

	public ToStringFromByteListCharsetFunction() {
		this(2, ACC_TYPES);
	}

	@Override
	public String getValue() {
		List<Byte> input = getInputValue(0);
		String charSet = getCharSet();
		byte[] bytes = new byte[input.size()];
		for (int i = 0; i < input.size(); i++) {
			bytes[i] = input.get(i).byteValue();
		}
		try {
			return new String(bytes, charSet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected String getCharSet() {
		return getInputValue(1);
	}

}