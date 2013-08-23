/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.core.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class DoubleHandler extends AbstractDataHandler<Double> {
	static protected List<String> types = new ArrayList<String>();
	static {
		types.add(SDFDatatype.DOUBLE.getURI());
		types.add(SDFDatatype.FLOAT.getURI());
		types.add("MV"); //??
	}

	@Override
	public IDataHandler<Double> getInstance(SDFSchema schema) {
		return new DoubleHandler();
	}

	public DoubleHandler() {
		super();
	}

	@Override
	public Double readData(ObjectInputStream inputStream) throws IOException {
		return inputStream.readDouble();
	}

	@Override
	public Double readData(String string) {
		if (string != null && string.length() > 0) {
			return Double.parseDouble(string);
		} else {
			return null;
		}
	}

	@Override
	public Double readData(ByteBuffer buffer) {
		double d = buffer.getDouble();
		// System.out.println("read Double Data: "+d);
		return d;
	}

	@Override
	public void writeData(List<String> output, Object data) {
		output.add(((Number) data).toString());
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		// System.out.println("write Double Data "+(Double)data);
		buffer.putDouble(((Number) data).doubleValue());
	}

	@Override
	final public List<String> getSupportedDataTypes() {
		return types;
	}

	@Override
	public int memSize(Object attribute) {
		return Double.SIZE / 8;
	}
	
	@Override
	public Class<?> createsType() {
		return Double.class;
	}
}
