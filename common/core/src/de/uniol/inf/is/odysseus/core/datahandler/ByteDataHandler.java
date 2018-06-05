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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Dennis Geesen
 * 
 */
public class ByteDataHandler extends AbstractDataHandler<Byte> {
	
	static protected List<String> types = new ArrayList<>();
	static{
		types.add(SDFDatatype.BYTE.getURI());		
	}

	public ByteDataHandler() {
		super(null);
	}
	
	@Override
	public Byte readData(ByteBuffer buffer) {
		byte b = buffer.get();
		return b;
	}

	@Override
	public Byte readData(String string) {
        if ((string == null)) {
            return null;
        }
        if (getConversionOptions().getNullValueString() != null) {
        	if (getConversionOptions().getNullValueString().equalsIgnoreCase(string)) {
        		return null;
        	}
        }
		return Byte.parseByte(string);		
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		byte b = (Byte) data;
		buffer.put(b);
	}

	@Override
	public int memSize(Object attribute) {
		return Byte.SIZE / 8;
	}

	@Override
	protected IDataHandler<Byte> getInstance(SDFSchema schema) {
		return new ByteDataHandler();
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}
	
	@Override
	public Class<?> createsType() {
		return Byte.class;
	}

}
