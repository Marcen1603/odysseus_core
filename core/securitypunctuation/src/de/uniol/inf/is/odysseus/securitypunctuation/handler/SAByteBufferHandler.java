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
package de.uniol.inf.is.odysseus.securitypunctuation.handler;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;

public class SAByteBufferHandler<T> extends ByteBufferHandler<T> {
	
	private IDataHandler<ISecurityPunctuation> securityPunctuationHandler;
	
	public SAByteBufferHandler(IDataHandler<T> dataHandler) {
		super(dataHandler);
	}

	@SuppressWarnings("unchecked")
	public synchronized T createSecurityAware(String spType) throws IOException, ClassNotFoundException, BufferUnderflowException {
		if(securityPunctuationHandler == null) {
			if(spType.equals("attribute")) {
				securityPunctuationHandler = new SecurityPunctuationHandler();
			} else {
				securityPunctuationHandler = new PredicateSPHandler();
			}
		}
		T retval = null;
		ByteBuffer byteBuffer = getByteBuffer();
		synchronized(byteBuffer){
			byteBuffer.flip();
			retval = (T)this.securityPunctuationHandler.readData(byteBuffer);
			byteBuffer.clear();
		}
		return retval;
	}

}
