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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface IDataHandler<T> {
	public T readData(ByteBuffer buffer);
	public T readData(ObjectInputStream inputStream) throws IOException;
	public T readData(String[] input);
	public T readData(List<String> input);
	public T readData(String string);
	public void writeData(ByteBuffer buffer, Object data);
	public List<String> getSupportedDataTypes();
	public int memSize(Object attribute);
	public IDataHandler<T> createInstance(SDFSchema schema);
	public IDataHandler<T> createInstance(List<String> schema);
	public boolean isPrototype();
	public void setPrototype(boolean p);
}
