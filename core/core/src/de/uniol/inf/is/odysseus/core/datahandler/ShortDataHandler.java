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


public class ShortDataHandler extends AbstractDataHandler<Short> {
	static protected List<String> types = new ArrayList<String>();
	static {		
		types.add("Short");		
	}

	@Override
	public IDataHandler<Short> getInstance(SDFSchema schema) {
		return new ShortDataHandler();
	}
	
	@Override
	public Short readData(ObjectInputStream inputStream) throws IOException {
		return inputStream.readShort();
	}
	
	@Override
	public Short readData(ByteBuffer buffer) {
		short i = buffer.getShort();
		return i;
	}

	@Override
	public Short readData(String string) {
		return Short.parseShort(string);
	}

    @Override
    public void writeData(List<String> output, Object data) {
        output.add(((Short) data).toString());
    }
    
	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		// System.out.println("write Integer Data "+(Integer)data);
		buffer.putShort(((Number) data).shortValue());
	}

	@Override
	final public List<String> getSupportedDataTypes() {		
		return types;
	}

	@Override
	public int memSize(Object attribute) {
		return Short.SIZE / 8;
	}

}
