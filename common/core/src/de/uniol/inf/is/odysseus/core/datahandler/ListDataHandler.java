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
 * @author Andr� Bolles
 *
 */
public class ListDataHandler extends AbstractDataHandler<List<?>>{

	static protected List<String> types = new ArrayList<String>();
	static{
		types.add("MULTI_VALUE"); //??
		types.add(SDFDatatype.LIST.getURI());
	}
	
	SDFSchema subType = null;
	IDataHandler<?> handler = null;
	
	public ListDataHandler(){
		// Needed for declarative service!
	}
	
	public ListDataHandler(SDFSchema subType){
		this.subType = subType;
		this.handler = DataHandlerRegistry.getDataHandler(this.subType.getAttribute(0).getAttributeName(), this.subType);

		//Is needed for handling of KeyValueObject
		if(this.handler == null && subType.getAttribute(0).getDatatype().getSubType() != null) {
			this.handler = DataHandlerRegistry.getDataHandler(this.subType.getAttribute(0).getDatatype().getSubType().toString(), this.subType);
		}
	}
	
	@Override
	public IDataHandler<List<?>> getInstance(SDFSchema schema) {
		return new ListDataHandler(schema);
	}
	
	@Override
	public List<?> readData(String string) {
		ArrayList<Object> values = new ArrayList<Object>();
		String[] lines = string.split("\n");
		int size = lines.length;
		for(int i = 0; i<size; i++){
			Object value = this.handler.readData(lines[i]);
			values.add(value);
		}
		return values;
	}

	@Override
	public List<?> readData(List<String> input) {
		ArrayList<Object> values = new ArrayList<Object>();
		for (String line:input){
			Object value = this.handler.readData(line);
			values.add(value);
		}
		return values;
	}
	
	public List<?> readData(String[] list, int from, int to) {
		ArrayList<Object> values = new ArrayList<Object>();
		for (int i=from; i<to;i++){
			values.add(list[i]);
		}
		return values;
	}

	public List<?> readData(List<String> list, int from, int to) {
		ArrayList<Object> values = new ArrayList<Object>();
		for (int i=from; i<to;i++){
			values.add(list.get(i));
		}
		return values;
	}

	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IDataHandler#readData(java.nio.ByteBuffer)
	 */
	@Override
	public List<?> readData(ByteBuffer buffer) {
		ArrayList<Object> values = new ArrayList<Object>();
		int size = buffer.getInt();
		for(int i = 0; i<size; i++){
			Object value = this.handler.readData(buffer);
			values.add(value);
		}
		return values;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IDataHandler#writeData(java.nio.ByteBuffer, java.lang.Object)
	 */
	@Override
	@SuppressWarnings({"rawtypes"})
	public void writeData(ByteBuffer buffer, Object data) {
		ArrayList values = (ArrayList)data;
		buffer.putInt(values.size());
		for(Object v: values){
			this.handler.writeData(buffer, v);
		}
		
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.access.AbstractDataHandler#getSupportedDataTypes()
	 */
	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}
	
	@Override
	public int memSize(Object data) {
		int size = 0;
		ArrayList<?> values = (ArrayList<?>)data;
		for(Object v: values){
			size+=this.handler.memSize(v);
		}
		return size;
	}
	
	@Override
	public Class<?> createsType() {
		return List.class;
	}

	
}
