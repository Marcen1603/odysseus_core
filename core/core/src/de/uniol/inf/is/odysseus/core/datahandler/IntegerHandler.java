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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


public class IntegerHandler extends AbstractDataHandler<Integer> {
	static protected List<String> types = new ArrayList<String>();
	static {
		types.add("Integer");
		types.add("Byte");
		types.add("Short");		
	}

	@Override
	public IDataHandler<Integer> getInstance(SDFSchema schema) {
		return new IntegerHandler();
	}
	
	@Override
	public Integer readData(ObjectInputStream inputStream) throws IOException {
		return inputStream.readInt();
	}
	
	@Override
	public Integer readData(ByteBuffer buffer) {
		int i = buffer.getInt();
		// System.out.println("read Int Data: "+i);
		return i;
	}

	@Override
	public Integer readData(String string) {
		return Integer.parseInt(string);
	}

    @Override
    public void writeData(List<String> output, Object data) {
        output.add(((Number) data).toString());
    }
    
	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		// System.out.println("write Integer Data "+(Integer)data);
		buffer.putInt(((Number) data).intValue());
	}

	@Override
	final public List<String> getSupportedDataTypes() {
		return types;
	}

	@Override
	public int memSize(Object attribute) {
		return Integer.SIZE / 8;
	}

}
