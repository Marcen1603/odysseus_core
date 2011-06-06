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
package de.uniol.inf.is.odysseus.spatial.access;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.InputStreamInStream;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;

import de.uniol.inf.is.odysseus.physicaloperator.access.AbstractAtomicDataHandler;

/**
 * @author André Bolles
 *
 */
public class SpatialByteHandler extends AbstractAtomicDataHandler{

	WKBReader reader;
	WKBWriter writer;
	
	public SpatialByteHandler(){
		this.reader = new WKBReader();
		this.writer = new WKBWriter();
	}
	
	@Override
	public Object readData() throws IOException {
		// the first four bytes are the length
		// of the geo data
		
		int length = this.getStream().readInt();
		byte[] binData = new byte[length];
		
		for (int i=0;i<length;i++){
			binData[i] = this.getStream().readByte();
		}
		
		try {
			return this.reader.read(binData);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	
	@Override
	public Object readData(ByteBuffer buffer) {
		// the first for bytes are the length of
		// the geo data
		int length = buffer.getInt();
		byte[] binData = new byte[length];
		
		for (int i=0;i<length;i++){
			binData[i] = buffer.get();
		}
		
		try {
			return this.reader.read(binData);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		byte[] binData = this.writer.write((Geometry)data);
		byte[] dataAndLength = new byte[binData.length];
		
		// split integer into 4 bytes
		byte[] length = this.intToByteArray(binData.length);
		
		// first put length
		System.arraycopy(length, 0, dataAndLength, 0, length.length);
		
		// then put data
		System.arraycopy(binData, 0, dataAndLength, length.length, binData.length);
		
		// put the data into the byte buffer
		buffer.put(dataAndLength);
	}
	
	private byte[] intToByteArray(int number){
		byte[] data = new byte[4];
		 
		for (int i = 0; i < 4; ++i) {
		    int shift = i << 3; // i * 8
		    data[3-i] = (byte)((number & (0xff << shift)) >>> shift);
		}
		
		return data;
		 
	}
	
	private int byteArrayToInt(byte[] data){
		// byte[] -> int
		int number = 0;     
		for (int i = 0; i < 4; ++i) {
		    number |= (data[3-i] & 0xff) << (i << 3);
		}
		
		return number;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.physicaloperator.access.IAtomicDataHandler#getName()
	 */
	@Override
	public String getName() {
		return "SpatialByteHandler";
	}
}
