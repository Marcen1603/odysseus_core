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
package de.uniol.inf.is.odysseus.core.datahandler;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


public class ObjectDataHandler<M> extends AbstractDataHandler<M> {

	static protected List<String> types = new ArrayList<String>();
	static{
		types.add(SDFDatatype.OBJECT.getURI());
	}
	
	@Override
	public IDataHandler<M> getInstance(SDFSchema schema) {
		return new ObjectDataHandler<M>();
	}
	
	@Override
	public M readData(ByteBuffer buffer) {
		return null;
	}

	@Override
	public M readData(String string) {
		return null;
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
	}

	@Override
	public int memSize(Object attribute) {
		return 0;
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}
	
	@Override
	public Class<?> createsType() {
		// Impossible to say ...
		return null;
	}

}
