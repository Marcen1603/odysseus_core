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
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

/**
 * @author DGeesen
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ParameterPresentationFactory {

	public static <V> IParameterPresentation<V> createPresentation(LogicalParameterInformation lpi, OperatorNode operator, V initialValue) {
		if (lpi.isList()) {
			IParameterPresentation<V> param = new ListParameterPresentation();
			param.init(lpi, operator, initialValue);
			return param;
		}
		IParameterPresentation<V> param = createPresentation(lpi.getParameterClass());
		param.init(lpi, operator, initialValue);
		return param;
	}

	public static <V> IParameterPresentation<V> createPresentationByClass(LogicalParameterInformation lpi, OperatorNode operator, V initialValue) {
		IParameterPresentation<V> param = createPresentation(lpi.getParameterClass());
		param.init(lpi, operator, initialValue);
		return param;

	}

	private static <V> IParameterPresentation createPresentation(Class<?> parameterClass) {
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
		if (parameterClass.getName().endsWith("builder.ResolvedSDFAttributeParameter")) {
			return new ResolvedSDFAttributeParameterPresentation();
		}
		if (parameterClass.getName().endsWith("builder.AggregateItemParameter")) {
			return new AggregateItemParameterPresentation();
		}
		if (parameterClass.getName().endsWith("builder.OptionParameter")) {
			return new OptionParameterPresentation();
		}
		if (parameterClass.getName().endsWith("builder.FileNameParameter")) {
			return new FilenameParameterPresentation();
		}
		if (parameterClass.getName().endsWith("builder.PredicateParameter")) {
			return new PredicateParameterPresentation();
		}		
		if (parameterClass.getName().endsWith("builder.SDFExpressionParameter")){
			return new SDFExpressionParameterPresentation();
		}
		if (parameterClass.getName().endsWith("builder.TimeParameter")){
			return new TimeParameterPresentation();
		}
		return new StringParameterPresentation();
	}

}
