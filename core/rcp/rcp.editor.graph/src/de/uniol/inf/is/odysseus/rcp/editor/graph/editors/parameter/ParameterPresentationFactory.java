/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;

/**
 * @author DGeesen
 * 
 */
public class ParameterPresentationFactory {

	public IParameterPresentation createPresentation(LogicalParameterInformation lpi) {
		if (lpi.isList()) {
			return new ListParameterPresentation();
		} else {
			return createPresentation(lpi.getParameterClass());
		}
	}

	public IParameterPresentation createPresentation(Class<?> parameterClass) {
		if (parameterClass.getName().endsWith("builder.IntegerParameter")) {
			return new IntegerParameterPresentation();
		}
		if (parameterClass.getName().endsWith("builder.LongParameter")) {
			return new IntegerParameterPresentation();
		}
		if (parameterClass.getName().endsWith("builder.ByteParameter")) {
			return new IntegerParameterPresentation();
		}
		if (parameterClass.getName().endsWith("builder.DoubleParameter")) {
			return new DoubleParameterPresentation();
		}
		if (parameterClass.getName().endsWith("builder.BooleanParameter")) {
			return new BooleanParameterPresentation();
		}
		if (parameterClass.getName().endsWith("builder.CreateSDFAttributeParameter")) {
			return new CreateSDFAttributeParameterPresentation();
		}
		return new StringParameterPresentation();
	}

}
