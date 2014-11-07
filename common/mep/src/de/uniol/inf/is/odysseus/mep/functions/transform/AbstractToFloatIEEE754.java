/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.functions.transform;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Converts two unsigned integer to a float value. with IEEE 754 (single
 * precision)
 * 
 * 
 * @author Marco Grawunder
 */
abstract public class AbstractToFloatIEEE754 extends AbstractFunction<Float> {

	private static final long serialVersionUID = 1201584883722391034L;

	public AbstractToFloatIEEE754(int arity, SDFDatatype[][] accTypes) {
		super("toFloat", arity, accTypes, SDFDatatype.FLOAT);
	}

	protected static Float calcValue(Integer v1, Integer v2, Boolean byteSwap) {
		byte[] bytes1 = intToByteArray(v1, byteSwap);
		byte[] bytes2 = intToByteArray(v2, byteSwap);

		ByteBuffer buffer = ByteBuffer.allocate(16);

		if (!byteSwap) {
			for (int i = 2; i < 4; i++) {
				buffer.put(bytes1[i]);
			}

			for (int i = 2; i <4; i++) {
				buffer.put(bytes2[i]);
			}			
		}else{
			for (int i = 0; i < 2; i++) {
				buffer.put(bytes1[i]);
			}

			for (int i = 0; i <2; i++) {
				buffer.put(bytes2[i]);
			}			
		}
		buffer.flip();

		return buffer.getFloat();
	}

	public static final byte[] intToByteArray(int value, boolean byteSwap) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.order(byteSwap ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
		return buffer.putInt(value).array();

	}

}
