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
package de.uniol.inf.is.odysseus.server.opcua.wrappers;

import java.util.List;

import com.xafero.turjumaan.server.java.api.Description;
import com.xafero.turjumaan.server.java.api.Format;
import com.xafero.turjumaan.server.java.api.NotCacheable;
import com.xafero.turjumaan.server.java.api.ResponseFormat;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;
import de.uniol.inf.is.odysseus.server.opcua.binding.OperatorServiceBinding;

/**
 * The operator of Odysseus.
 */
@Description("The operator of Odysseus")
public class Operator {

	/**
	 * Gets the operator builder.
	 *
	 * @return the operator builder
	 */
	@NotCacheable
	@ResponseFormat(Format.XML)
	@Description("Its build operator")
	public List<IOperatorBuilder> getOperatorBuilder() {
		return getOperator().getOperatorBuilder();
	}

	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	private IOperatorBuilderFactory getOperator() {
		return OperatorServiceBinding.getOperator();
	}
}