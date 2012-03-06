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

public class StringHandler extends AbstractAtomicDataHandler {
	static protected List<String> types = new ArrayList<String>();
	static{
		types.add("String");
	}
	
	@Override
	public Object readData() throws IOException {
		return stream.readUTF();
	}
	
	@Override
	public Object readData(ObjectInputStream inputStream) throws IOException {
		return inputStream.readUTF();
	}

	@Override
	public Object readData(ByteBuffer b) {
		StringBuffer buf = new StringBuffer();
		int size = b.getInt();
		//System.out.println("size "+size);
		for (int i=0;i<size;i++){
			char c = b.getChar();
			//System.out.print(c);
			buf.append(c);
		}
		//System.out.println();
		return buf.toString();
	}
	
	@Override
	public Object readData(String string) {
		return string;
	}
	
	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		String s = (String) data;
		//System.out.println("write String Data "+s+" "+s.length());		
		buffer.putInt(s.length());
		for (int i=0;i<s.length();i++){
			buffer.putChar(s.charAt(i));
		}
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}
	
	@Override
	public int memSize(Object attribute) {
		return ((String) attribute).length() * 2 // Unicode!
				+ Integer.SIZE / 8; // F�r die L�ngeninformation (evtl.
									// anders machen?)
	}
}
