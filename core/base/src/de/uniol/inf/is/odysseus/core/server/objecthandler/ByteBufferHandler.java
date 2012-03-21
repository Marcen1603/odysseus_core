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
package de.uniol.inf.is.odysseus.core.server.objecthandler;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.server.datahandler.IDataHandler;

public class ByteBufferHandler<T> implements
		IObjectHandler<T> {

	ByteBuffer byteBuffer = null;
	private IDataHandler<?> dataHandler;
		
	public ByteBufferHandler(IDataHandler<?> dataHandler) {
		byteBuffer = ByteBuffer.allocate(1024);
		this.dataHandler = dataHandler;		
	}
	
	public ByteBufferHandler(
			ByteBufferHandler<T> objectHandler) {
		super();
		this.dataHandler = objectHandler.dataHandler;
	}
	
	@Override
	public void clear() {
		byteBuffer.clear();
	}

	@Override
	public ByteBuffer getByteBuffer(){
		return byteBuffer;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized T create() throws IOException, ClassNotFoundException, BufferUnderflowException {
		T retval = null;
		synchronized(byteBuffer){		
			byteBuffer.flip();
			retval = (T)this.dataHandler.readData(byteBuffer);
			byteBuffer.clear();
		}
		return retval;
	}
	
	private void checkOverflow(ByteBuffer buffer, int size) {
		if (size+byteBuffer.position()>=byteBuffer.capacity()){
			// TODO: Effizientere �berlaufbehandlung?
			//logger.warn("ObjectHandler OVERFLOW");
			ByteBuffer newBB = ByteBuffer.allocate((buffer.limit()+size+byteBuffer.position())*2);
			newBB.put(byteBuffer);
			byteBuffer = newBB;
		}
	}
	
	@Override
	public void put(ByteBuffer buffer) throws IOException {
		synchronized(buffer){
			synchronized(byteBuffer){
				//System.out.println("putBuffer "+buffer+" to "+byteBuffer);
				checkOverflow(buffer, buffer.remaining());
				byteBuffer.put(buffer);
				//System.out.println("putBuffer "+buffer+" to "+byteBuffer);
			}
		}
	}
	
	@Override
	public void put(ByteBuffer buffer, int size) throws IOException {
		synchronized(buffer){
			synchronized(byteBuffer){
				//System.out.println("putBuffer2 "+buffer+" to "+byteBuffer);
				checkOverflow(buffer, size);
				// Why copy each single byte?
//				for (int i=0;i<size;i++){
//					byteBuffer.put(buffer.get());
//				}
				byteBuffer.put(buffer.array(), buffer.position(), size);
				buffer.position(buffer.position() + size);
				//System.out.println("putBuffer2 "+buffer+" to "+byteBuffer);
			}
		}
		
	}

	@Override
	public void put(T value) {

		synchronized(byteBuffer){
			
			byteBuffer.clear();
			
			this.dataHandler.writeData(byteBuffer, value);
			

			byteBuffer.flip();
		}
	}
	
	@Override
	public ByteBufferHandler<T> clone() {
		return new ByteBufferHandler<T>(this);
	}


}
