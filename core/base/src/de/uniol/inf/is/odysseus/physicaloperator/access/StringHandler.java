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

public class StringHandler extends AbstractAtomicDataHandler {
	static{
		types.add("String");
	}
	
	@Override
	public Object readData() throws IOException {
		return getStream().readUTF();
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
	public void writeData(ByteBuffer buffer, Object data) {
		String s = (String) data;
		//System.out.println("write String Data "+s+" "+s.length());		
		buffer.putInt(s.length());
		for (int i=0;i<s.length();i++){
			buffer.putChar(s.charAt(i));
		}
	}

}
