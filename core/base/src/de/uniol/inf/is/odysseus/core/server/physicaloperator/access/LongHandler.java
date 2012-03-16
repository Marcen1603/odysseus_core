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

public class LongHandler extends AbstractDataHandler {
	static protected List<String> types = new ArrayList<String>();
	static{
		types.add("Long");
		types.add("Timestamp");
		types.add("StartTimestamp");
		types.add("EndTimestamp");
	}
	
	@Override
	public final Object readData() throws IOException {
		return stream.readLong();
	}

	@Override
	public Object readData(ObjectInputStream inputStream) throws IOException {
		return inputStream.readLong();
	}
	
	@Override
	public Object readData(ByteBuffer buffer) {
		long l = buffer.getLong();
		//System.out.println("read Long Data: "+l);
		return l;
	}
	
	@Override
	public Object readData(String string) {
		return Long.parseLong(string);
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		//System.out.println("write Long Data "+((Number)data).longValue());
		buffer.putLong(((Number)data).longValue());
	}	

	@Override
	final public List<String> getSupportedDataTypes() {
		return types;
	}
	
	@Override
	public int memSize(Object attribute) {
		return Long.SIZE / 8;
	}
}
