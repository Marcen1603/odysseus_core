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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import de.uniol.inf.is.odysseus.core.objecthandler.ObjectByteConverter;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


public class ObjectDataHandler extends AbstractDataHandler<Object> {

	static protected List<String> types = new ArrayList<String>();
	static{
		types.add(SDFDatatype.OBJECT.getURI());
	}

	@Override
	public IDataHandler<Object> getInstance(SDFSchema schema) {
		return new ObjectDataHandler();
	}

	@Override
	public Object readData(ByteBuffer buffer) {
		int size = buffer.getInt();
		byte[] data = new byte[size];
		buffer.get(data);
		Object o = ObjectByteConverter.bytesToObject(data);
		return  o;
	}

	@Override
	public Object readData(String string) {
		String val = Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
		Object o = ObjectByteConverter.bytesToObject(val.getBytes());
		return o;
	}

	@Override
	public void writeData(StringBuilder string, Object data) {
		byte[] bytes = ObjectByteConverter.objectToBytes(data);
		byte[] decoded = Base64.getDecoder().decode(bytes);
		String val = new String(decoded, StandardCharsets.UTF_8);
		string.append(val);
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		byte[] bytes = ObjectByteConverter.objectToBytes(data);
		buffer.putInt(bytes.length);
		buffer.put(bytes);
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
		return Object.class;
	}

}
