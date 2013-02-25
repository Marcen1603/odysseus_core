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
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


public class StringHandler extends AbstractDataHandler<String> {
	static protected List<String> types = new ArrayList<String>();
	static{
		types.add("String");
		types.add("StartTimestampString");
	}
	private static Charset charset = Charset.forName("UTF-8");
	private static CharsetDecoder decoder = charset.newDecoder();
	private static CharsetEncoder encoder = charset.newEncoder();
	
	@Override
	public IDataHandler<String> getInstance(SDFSchema schema) {
		return new StringHandler();
	}
	
	@Override
	public String readData(ObjectInputStream inputStream) throws IOException {
		return inputStream.readUTF();
	}

	@Override
	public String readData(ByteBuffer b) {
		// StringBuffer buf = new StringBuffer();
		int size = b.getInt();
		// System.out.println("size "+size);
		int limit = b.limit();
		b.limit(b.position() + size);
		try {
			CharBuffer decoded = decoder.decode(b);
			return decoded.toString();
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}finally{
			b.limit(limit);
		}
		
		// for (int i=0;i<size;i++){
		// char c = b.getChar();
		// //System.out.print(c);
		// buf.append(c);
		// }
		// System.out.println();
		// return buf.toString();
		return "";
	}
	
	@Override
	public String readData(String string) {
		return string;
	}

    @Override
    public void writeData(List<String> output, Object data) {
        output.add((String) data);
    }
    
	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		String s = (String) data;
		// System.out.println("write String Data "+s+" "+s.length());
		// buffer.putInt(s.length());
		try {
			ByteBuffer charBuffer = encoder.encode(CharBuffer.wrap(s));
			buffer.putInt(charBuffer.limit());
			buffer.put(charBuffer);
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
		// for (int i=0;i<s.length();i++){
		// buffer.putChar(s.charAt(i));
		// }
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}
	
	@Override
	public int memSize(Object attribute) {
		try {
			int val = encoder.encode(CharBuffer.wrap((String)attribute)).limit()+Integer.SIZE/8; 
			return val;
		} catch (CharacterCodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Integer.SIZE/8;
//		return ((String) attribute).length() * 2 // Unicode!
//				+ Integer.SIZE / 8; // F�r die L�ngeninformation (evtl.
//									// anders machen?)
	}
}
