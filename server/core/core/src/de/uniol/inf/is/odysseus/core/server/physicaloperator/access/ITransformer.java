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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface ITransformer<IN, OUT> {

	/**
	 * Transforms the input to output that will be returned
	 * @param input
	 * @return
	 */
	public OUT transform(IN input);
	
	/**
	 * Creates a new instance of this transformer. 
	 * @param options: potential options needed for this transformer, can be null
	 * @param schema: potential schema, can be null
	 * @return a new instance of this transformer
	 */
	public ITransformer<IN, OUT> getInstance(Map<String, String> options, SDFSchema schema);

	
	public String getName();
	
}
