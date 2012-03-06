/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author André Bolles
 *
 */
public class SetDataHandler extends AbstractAtomicDataHandler{

	static protected List<String> types = new ArrayList<String>();
	static{
		types.add("MULTI_VALUE");
	}
	
	SDFDatatype subType = null;
	IAtomicDataHandler handler = null;
	
	public SetDataHandler(SDFDatatype subType){
		this.subType = subType;
		this.handler = DataHandlerRegistry.getDataHandler(this.subType.getURI());
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IAtomicDataHandler#readData()
	 */
	@Override
	public Object readData() throws IOException {
		return readData(stream);
	}
	
	@Override
	public Object readData(ObjectInputStream inputStream) throws IOException {
		ArrayList<Object> values = new ArrayList<Object>();
		int size = inputStream.readInt();
		for(int i = 0; i<size; i++){
			Object value = this.handler.readData(inputStream);
			values.add(value);
		}
		
		return values;
	}
	
	@Override
	public Object readData(String string) {
		throw new RuntimeException("Sorry. Currently not implemented");
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IAtomicDataHandler#readData(java.nio.ByteBuffer)
	 */
	@Override
	public Object readData(ByteBuffer buffer) {
		ArrayList<Object> values = new ArrayList<Object>();
		int size = buffer.getInt();
		for(int i = 0; i<size; i++){
			Object value = this.handler.readData(buffer);
			values.add(value);
		}
		return values;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IAtomicDataHandler#writeData(java.nio.ByteBuffer, java.lang.Object)
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
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.access.AbstractAtomicDataHandler#getSupportedDataTypes()
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

	
}
