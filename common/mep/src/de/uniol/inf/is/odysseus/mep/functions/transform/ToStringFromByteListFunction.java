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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * Input parameter byte list, output string. Charset is utf-8
 * 
 * @author Michael Brand <michael.brand@uol.de>
 *
 */
public class ToStringFromByteListFunction extends ToStringFromByteListCharsetFunction {

	private static final long serialVersionUID = -7752170243228474800L;
	
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFDatatype.LIST_BYTE } };

	public ToStringFromByteListFunction() {
		super(1, ACC_TYPES);
	}

	@Override
	protected String getCharSet() {
		return "utf-8";
	}

}