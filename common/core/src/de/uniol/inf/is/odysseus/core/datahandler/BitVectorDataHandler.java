/*******************************************************************************
 * Copyright 2014 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.core.datahandler;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.BitVector;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Marco Grawunder
 *
 */

public class BitVectorDataHandler extends AbstractDataHandler<BitVector> {
	
	static protected List<String> types = new ArrayList<>();
	static {		
		types.add(SDFDatatype.BITVECTOR.getURI());
	}

	public BitVectorDataHandler() {
		super(null);
	}
	
	@Override
	public IDataHandler<BitVector> getInstance(SDFSchema schema) {
		return new BitVectorDataHandler();
	}
	
	@Override
	public BitVector readData(ByteBuffer buffer) {
		int size = buffer.getInt();
		int bytesize = buffer.getInt();
		byte[] bytes = new byte[bytesize];
		buffer.get(bytes);
		int i = buffer.getInt();
		boolean isMSBAccess = i==0?false:true;
		return new BitVector(size, bytes, isMSBAccess);
	}
	
	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		BitVector v = (BitVector) data;
		buffer.putInt(v.size());
		byte[] bytes = v.getBytes();
		buffer.putInt(bytes.length);
		buffer.put(v.getBytes());
		buffer.putInt(v.isMSBAccess()?1:0);
	}

	@Override
	public BitVector readData(String input) {
		// Format: 11001101 00000101 00000000
		char[] a = input.toCharArray();
		// count all element without blank
		int size = 0;
		for (char c:a){
			if (c == '0' || c == '1'){
				size++;
			}
		}
		// Set bits
		BitVector v = new BitVector(size);
		int index = 0;
		for (char c:a){
			if (c == '0'){
				v.setBit(index++, false);			
			}else if (c == '1'){
				v.setBit(index++, true);							
			}
		} 
		return v;
	}

	@Override
	public int memSize(Object attribute) {
		BitVector v = (BitVector) attribute;
		// size, length, bytes, boolean
		int val = Integer.SIZE / 8 + Integer.SIZE / 8 +
				v.getBytes().length+ Integer.SIZE / 8;
		return val;
	}

	@Override
	public Class<?> createsType() {
		return BitVector.class;
	}


	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

}
