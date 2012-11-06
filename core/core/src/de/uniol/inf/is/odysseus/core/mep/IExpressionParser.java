/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.mep;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * This interface encapsulates different expression parsers
 * 
 * @author Marco Grawunder
 *
 */
public interface IExpressionParser {

	/**
	 * Parse a String expression
	 * 
	 * @param expressionStr The expression to parse
	 * @param schema The {@link SDFSchema schema} of the expression
	 * @return an IExpression object that contains the parsed expression
	 * @throws ParseException 
	 */
	IExpression<?> parse(String expressionStr, SDFSchema schema) throws ParseException;

}
