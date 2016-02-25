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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Andr� Bolles
 *
 */
public class ListDataHandler extends AbstractDataHandler<List<?>>{
	
	protected static Logger LOG = LoggerFactory.getLogger(ListDataHandler.class);

	static protected List<String> types = new ArrayList<String>();
	static{
		types.add("MULTI_VALUE"); //??
		for (SDFDatatype d: SDFDatatype.LISTS){
			types.add(d.getURI());
		}
	}
	
	IDataHandler<?> handler = null;
	final private boolean nullMode;
	
	public ListDataHandler(){
		// Needed for declarative service!
		nullMode = false;
	}
	
	public ListDataHandler(SDFSchema subType){

		// hmmm ... this should be more generic !!
		if (subType.getAttribute(0).getDatatype().isTuple() || subType.getAttribute(0).getDatatype().getSubType().isTuple()){
			this.handler = DataHandlerRegistry.getDataHandler("TUPLE", subType.getAttribute(0).getDatatype().getSchema());
		}else{
			this.handler = DataHandlerRegistry.getDataHandler(subType.getAttribute(0).getAttributeName(), subType);
		}

		//Is needed for handling of KeyValueObject
		if(this.handler == null && subType.getAttribute(0).getDatatype().getSubType() != null) {
			SDFDatatype localSubType = subType.getAttribute(0).getDatatype().getSubType();
			if (localSubType.isTuple()){
				this.handler = DataHandlerRegistry.getDataHandler(SDFDatatype.TUPLE.toString(), subType.getAttribute(0).getDatatype().getSchema());
			}else{
				this.handler = DataHandlerRegistry.getDataHandler(subType.getAttribute(0).getDatatype().getSubType().toString(), subType);
			}
		}
		
		nullMode = false;
	}
	
	@Override
	public IDataHandler<List<?>> getInstance(SDFSchema schema) {
		return new ListDataHandler(schema);
	}
	
	@Override
	public List<?> readData(String string) {
		if(string == null) {
			return null;
		}
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
			Object value = this.handler.readData(list[i]);
			values.add(value);
		}
		return values;
	}

	public List<?> readData(List<String> list, int from, int to) {
		ArrayList<Object> values = new ArrayList<Object>();
		for (int i=from; i<to;i++){
			Object value = this.handler.readData(list.get(i));
			values.add(value);
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
			byte type = -1;
			if (nullMode) {
				type = buffer.get();
			}
			if (!nullMode || type != 0) {
				Object value = this.handler.readData(buffer);
				values.add(value);
			} else {
				values.add(null);
			}
		}
		return values;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IDataHandler#writeData(java.nio.ByteBuffer, java.lang.Object)
	 */
	@Override
	@SuppressWarnings({"rawtypes"})
	public void writeData(ByteBuffer buffer, Object data) {
		List values = (List)data;
		buffer.putInt(values.size());
		for(Object v: values){
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
		List<?> values = (List<?>)data;
		for(Object v: values){
			size+=this.handler.memSize(v);
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
