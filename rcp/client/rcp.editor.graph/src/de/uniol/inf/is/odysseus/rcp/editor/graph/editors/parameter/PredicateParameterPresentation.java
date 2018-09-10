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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

/**
 * @author DGeesen
 * 
 */
public class PredicateParameterPresentation extends StringParameterPresentation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#getPQLString()
	 */
	@Override
	public String getPQLString() {
		if (getValue() != null) {
			@SuppressWarnings("rawtypes")
			Class<? extends IStreamObject> datamodel = getOperator().getInputSchemas().get(0).getType();
			if (datamodel.equals(Tuple.class)) {
				if (!getValue().startsWith("RelationalPredicate")) {					
					return "RelationalPredicate('" + getValue() + "')";
				}
			}
			return getValue();
		}
		return "";
	}


}
