/*******************************************************************************
 * Copyright 2016 Georg Berendt
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
package de.uniol.inf.is.odysseus.wrapper.opcua.access;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.wrapper.opcua.func.ErrorFunction;
import de.uniol.inf.is.odysseus.wrapper.opcua.func.QualityFunction;
import de.uniol.inf.is.odysseus.wrapper.opcua.func.TimestampFunction;
import de.uniol.inf.is.odysseus.wrapper.opcua.func.ToErrorStrFunction;
import de.uniol.inf.is.odysseus.wrapper.opcua.func.ValueFunction;

/**
 * The OPC UA function provider.
 */
public class OPCUAFunctionProvider implements IFunctionProvider {

	@SuppressWarnings("rawtypes")
	@Override
	public List<IMepFunction<?>> getFunctions() {
		return Arrays.asList(new ErrorFunction(), new QualityFunction(), new TimestampFunction(),
				new ToErrorStrFunction(), new ValueFunction());
	}
}