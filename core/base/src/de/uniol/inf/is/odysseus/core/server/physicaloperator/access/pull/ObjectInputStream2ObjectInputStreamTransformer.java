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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.io.ObjectInputStream;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IToObjectInputStreamTransformer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.ITransformer;


public class ObjectInputStream2ObjectInputStreamTransformer implements
		IToObjectInputStreamTransformer<ObjectInputStream> {

	public ObjectInputStream2ObjectInputStreamTransformer() {
	}
	
	@Override
	public ITransformer<ObjectInputStream, ObjectInputStream> getInstance(
			Map<String, String> options, SDFSchema schema) {
		return new ObjectInputStream2ObjectInputStreamTransformer();
	}
	
	@Override
	public ObjectInputStream transform(ObjectInputStream input) {
		return input;
	}

	@Override
	public String getName() {
		return "ObjectInputStream2ObjectInputStream";
	}
}
