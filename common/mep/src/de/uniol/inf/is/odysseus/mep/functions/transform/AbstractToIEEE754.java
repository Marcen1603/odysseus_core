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
 * Converts walues with IEEE 754 
 * 
 * 
 * @author Marco Grawunder
 */
abstract public class AbstractToIEEE754<T> extends AbstractFunction<T> {

	private static final long serialVersionUID = 1201584883722391034L;

	public AbstractToIEEE754(String name,int arity, SDFDatatype[][] accTypes, SDFDatatype returnType) {
		super(name, arity, accTypes, returnType);
	}

	public static final byte[] intToByteArray(int value, boolean byteSwap) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.order(byteSwap ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
		return buffer.putInt(value).array();

	}

}
