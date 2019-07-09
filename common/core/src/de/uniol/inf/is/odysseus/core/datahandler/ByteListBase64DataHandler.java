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
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.ConversionOptions;
import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * This data handler is especially for list of bytes that need to be encoded with Base64.
 * 
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class ByteListBase64DataHandler extends AbstractDataHandler<List<Byte>> {

	protected static Logger LOG = LoggerFactory.getLogger(ByteListBase64DataHandler.class);

	static protected List<String> types = new ArrayList<String>();

	static {
		types.add(SDFDatatype.LIST_BYTE_BASE64.getURI());
	}

	ByteDataHandler handler = null;
	final private boolean nullMode;

	public ByteListBase64DataHandler() {
		// Needed for declarative service!
		nullMode = false;
	}

	public ByteListBase64DataHandler(SDFSchema subType) {
		handler = (ByteDataHandler) getDataHandler(SDFDatatype.BYTE, subType);
		nullMode = false;
	}

	@Override
	public void setConversionOptions(ConversionOptions conversionOptions) {
		super.setConversionOptions(conversionOptions);
		if (this.handler != null) {
			this.handler.setConversionOptions(conversionOptions);
		}
	}

	@Override
	public IDataHandler<List<Byte>> getInstance(SDFSchema schema) {
		return new ByteListBase64DataHandler(schema);
	}

	@Override
	public List<Byte> readData(String string) {
		if (string == null) {
			return null;
		}
		
		byte[] bytes = Base64.getDecoder().decode(string);		
		ArrayList<Byte> returnValues = new ArrayList<Byte>(bytes.length);
		for(byte b : bytes) {
			returnValues.add(b);
		}
		return returnValues;
	}

	@Override
	public List<Byte> readData(Iterator<String> input) {
		if (input == null) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		while(input.hasNext()) {
			buffer.append(input.next());
		}
		
		byte[] bytes = Base64.getDecoder().decode(buffer.toString());		
		ArrayList<Byte> returnValues = new ArrayList<Byte>(bytes.length);
		for(byte b : bytes) {
			returnValues.add(b);
		}
		return returnValues;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IDataHandler
	 * #readData(java.nio.ByteBuffer)
	 */
	@Override
	public List<Byte> readData(ByteBuffer buffer) {
		ArrayList<Byte> values = new ArrayList<Byte>();
		int size = buffer.getInt();
		for (int i = 0; i < size; i++) {
			byte type = -1;
			if (nullMode) {
				type = buffer.get();
			}
			if (!nullMode || type != 0) {
				Byte value = this.handler.readData(buffer);
				values.add(value);
			} else {
				values.add(null);
			}
		}
		return values;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IDataHandler
	 * #writeData(java.nio.ByteBuffer, java.lang.Object)
	 */
	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		@SuppressWarnings("unchecked")
		List<Byte> values = (List<Byte>) data;
		buffer.putInt(values.size());
		for (Byte v : values) {
			if (nullMode) {
				if (v == null) {
					buffer.put((byte) 0);
				} else {
					buffer.put((byte) 1);
				}
			}
			if (!nullMode || (nullMode && v != null)) {
				this.handler.writeData(buffer, v);
			}
		}

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void writeData(StringBuilder string, Object data) {
		if(data == null) {
    		return;
    	}
    	if(data instanceof List) {
    		List<?> list = (List<?>) data;
    		if(list.isEmpty()) {
    			string.append("");
    		} else if(list.get(0) instanceof Integer) {
    			string.append(writeDataFromIntegers((List<Integer>) data)); 
    		} else {
    			string.append(writeDataFromBytes((List<Byte>) data));
    		}
    	}
	}
		
    @SuppressWarnings("unchecked")
	@Override
    public void writeData(List<String> output, Object data, WriteOptions options) {
    	if(data == null) {
    		return;
    	}
    	if(data instanceof List) {
    		List<?> list = (List<?>) data;
    		if(list.isEmpty()) {
    			output.add("");
    		} else if(list.get(0) instanceof Integer) {
    			output.add(writeDataFromIntegers((List<Integer>) data)); 
    		} else {
    			output.add(writeDataFromBytes((List<Byte>) data));
    		}
    	}
    }
    
    private String writeDataFromBytes(List<Byte> data) {
    	List<Byte> values = (List<Byte>) data;
		byte[] bytes = new byte[values.size()];
		for(int i = 0; i  < values.size(); i++) {
			Byte value = values.get(i);
			if(value == null) {
				bytes[i] = 0;
			} else {
				bytes[i] = value.byteValue();
			}
		}
	    return Base64.getEncoder().encodeToString(bytes);
    }
    
    private String writeDataFromIntegers(List<Integer> data) {
    	List<Integer> values = (List<Integer>) data;
		byte[] bytes = new byte[values.size()];
		for(int i = 0; i  < values.size(); i++) {
			Integer value = values.get(i);
			if(value == null) {
				bytes[i] = 0;
			} else {
				bytes[i] = value.byteValue();
			}
		}
	    return Base64.getEncoder().encodeToString(bytes);
    }

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.access.
	 * AbstractDataHandler#getSupportedDataTypes()
	 */
	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

	@Override
	public int memSize(Object data) {
		int size = 0;
		@SuppressWarnings("unchecked")
		List<Byte> values = (List<Byte>) data;
		for (Byte v : values) {
			size += this.handler.memSize(v);
		}
		// Marker for null or not null values
		if (nullMode) {
			size += values.size();
		}
		return size;
	}

	@Override
	public Class<?> createsType() {
		return List.class;
	}

}
