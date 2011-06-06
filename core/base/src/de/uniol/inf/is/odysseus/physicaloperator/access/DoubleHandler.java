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
package de.uniol.inf.is.odysseus.physicaloperator.access;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DoubleHandler extends AbstractAtomicDataHandler {

	/**
	 * 
	 */
	public DoubleHandler() {
		super();
	}
	
	@Override
	final public Object readData() throws IOException {
		return getStream().readDouble();
	}

	@Override
	public Object readData(ByteBuffer buffer) {
		double d = buffer.getDouble();
		//System.out.println("read Double Data: "+d);
		return d;
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		//System.out.println("write Double Data "+(Double)data);
		buffer.putDouble(((Number)data).doubleValue());		
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.physicaloperator.access.IAtomicDataHandler#getDataTypeName()
	 */
	@Override
	public List<String> getSupportedDataTypes() {
		List<String> types = new ArrayList<String>();
		types.add("Double");
		types.add("Float");
		types.add("MV");
		return types;
	}
	

}
