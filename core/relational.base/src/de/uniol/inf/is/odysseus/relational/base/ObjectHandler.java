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
package de.uniol.inf.is.odysseus.relational.base;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.access.IAtomicDataHandler;
import de.uniol.inf.is.odysseus.physicaloperator.access.IObjectHandler;

public class ObjectHandler<T extends IMetaAttributeContainer<M>, M extends IMetaAttribute> implements
		IObjectHandler<T> {

	//private static final Logger logger = LoggerFactory.getLogger( ObjectHandler.class );
	ByteBuffer byteBuffer = null;
	private IAtomicDataHandler dataHandler;
		
	public ObjectHandler(IAtomicDataHandler dataHandler) {
		byteBuffer = ByteBuffer.allocate(1024);
		this.dataHandler = dataHandler;		
	}
	
	public ObjectHandler(
			ObjectHandler<T, M> objectHandler) {
		super();
		this.dataHandler = objectHandler.dataHandler;
	}
	
	@Override
	public void clear() {
		byteBuffer.clear();
	}

//	private void createDataReader(SDFAttributeList schema) {
//		this.dataHandler = new IAtomicDataHandler[schema.size()];
//		int i = 0;
//		for (SDFAttribute attribute : schema) {
//			String uri = attribute.getDatatype().getURI(false);
//			IAtomicDataHandler handler = DataHandlerRegistry.getDataHandler(uri);
//			
//			if(handler == null){
//				throw new RuntimeException("illegal datatype "+uri);
//			}
//			
//			this.dataHandler[i++] = handler;
//			
//			if (uri.equals("Integer")) {
//				this.dataHandler[i++] = DataHandlerRegistry.getDataHandler("IntegerHandler");
//			} else if (uri.equals("Long") || uri.endsWith("Timestamp")) {
//				this.dataHandler[i++] = DataHandlerRegistry.getDataHandler("LongHandler");
//			} else if (uri.equals("Double")) {
//				this.dataHandler[i++] = DataHandlerRegistry.getDataHandler("DoubleHandler");
//			} else if (uri.equals("String")) {
//				this.dataHandler[i++] = DataHandlerRegistry.getDataHandler("StringHandler");
//			} else if (uri.equalsIgnoreCase("SpatialPoint") ||
//					uri.equalsIgnoreCase("SpatialLine") ||
//					uri.equalsIgnoreCase("SpatialPolygon") || 
//					uri.equalsIgnoreCase("SpatialMulitPoint") ||
//					uri.equalsIgnoreCase("SpatialMultiLine") ||
//					uri.equalsIgnoreCase("SpatialMultiPolygon")){
//				this.dataHandler[i++] = DataHandlerRegistry.getDataHandler("SpatialByteHandler");
//				
//				try {
//					Class clazz = Class.forName("de.uniol.inf.is.odysseus.spatial.access.SpatialByteHandler");
//					this.dataHandler[i++] = (IAtomicDataHandler)clazz.newInstance();
//				} catch (InstantiationException e) {
//					throw new RuntimeException(e);
//				} catch (IllegalAccessException e) {
//					throw new RuntimeException(e);
//				} catch (ClassNotFoundException e) {
//					throw new RuntimeException(e);
//				}
//			} else {
//				throw new RuntimeException("illegal datatype "+uri);
//			}
//		}
//	}


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
				for (int i=0;i<size;i++){
					byteBuffer.put(buffer.get());
				}
				//System.out.println("putBuffer2 "+buffer+" to "+byteBuffer);
			}
		}
		
	}
	
//	public void put(int pos, Object val){
//		synchronized(byteBuffer){
//			dataHandler[pos].writeData(byteBuffer, val);
//		}
//	}
	
	@Override
	public void put(T value) {

		synchronized(byteBuffer){
			
			byteBuffer.clear();
			
			this.dataHandler.writeData(byteBuffer, value);
			
//			for (int i=0;i<dataHandler.length;i++){
//				dataHandler[i].writeData(byteBuffer, relationalTuple.getAttribute(i));
//			}
			byteBuffer.flip();
		}
	}
	
	@Override
	public ObjectHandler<T,M> clone() {
		return new ObjectHandler<T,M>(this);
	}
//	@SuppressWarnings("unchecked")
//	public static void main(String[] args) throws IOException, ClassNotFoundException {
//		SDFAttributeList schema = new SDFAttributeList();
//		SDFAttribute a = new SDFAttribute("a_int");
//		a.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("Integer"));
//		schema.add(a);
//		a = new SDFAttribute("a_long");
//		a.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("Long"));
//		schema.add(a);
//		a = new SDFAttribute("a_double");
//		a.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("Double"));
//		schema.add(a);
//		a = new SDFAttribute("a_String");
//		a.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("String"));
//		schema.add(a);
//		ObjectHandler h = new ObjectHandler(schema);
//		h.put(0,10);
//		h.put(1,100l);
//		h.put(2,100.0d);
//		h.put(3,"Hallo Folks");
//			
//		h.getByteBuffer();
//		RelationalTuple<IMetaAttribute> r = h.create();
//		System.out.println(r);
//		
//	}


}
