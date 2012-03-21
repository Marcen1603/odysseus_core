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
package de.uniol.inf.is.odysseus.objecttracking;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.server.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.server.objecthandler.IObjectHandler;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

public class MVRelationalTupleObjectHandler<M extends IProbability> implements
		IObjectHandler<Tuple<M>> {

	//private static final Logger logger = LoggerFactory.getLogger( ObjectHandler.class );
	ByteBuffer byteBuffer = null;
	private IDataHandler<?>[] dataHandler;
		
	public MVRelationalTupleObjectHandler(SDFSchema schema) {
		byteBuffer = ByteBuffer.allocate(1024);
		createDataReader(schema);		
	}
	
	public MVRelationalTupleObjectHandler(
			MVRelationalTupleObjectHandler<M> relationalTupleObjectHandler) {
		super();
		if (relationalTupleObjectHandler.dataHandler != null){
			int l = relationalTupleObjectHandler.dataHandler.length;
			dataHandler = new IDataHandler[l];
			System.arraycopy(relationalTupleObjectHandler.dataHandler, 0, dataHandler, 0, l);
		}
	}
	
	@Override
	public void clear() {
		byteBuffer.clear();
	}

	private void createDataReader(SDFSchema schema) {
		this.dataHandler = new IDataHandler[schema.size()];
		int i = 0;
		for (SDFAttribute attribute : schema) {
			String uri = attribute.getDatatype().getURI(false);
			
			IDataHandler<?> handler = DataHandlerRegistry.getDataHandler(uri);
			if(handler == null){
				throw new IllegalArgumentException("No handler for datatype "+ uri);
			}
            this.dataHandler[i++] = handler;
			
//			if (uri.equals("Integer")) {
//				this.dataHandler[i++] = new IntegerHandler();
//			} else if (uri.equals("Long") || uri.endsWith("Timestamp")) {
//				this.dataHandler[i++] = new LongHandler();
//			} else if (uri.equals("Double") || uri.equals("MV")) {
//				this.dataHandler[i++] = new DoubleHandler();
//			} else if (uri.equals("String")) {
//				this.dataHandler[i++] = new StringHandler();
//			} else {
//				throw new RuntimeException("illegal datatype");
//			}
		}
	}


	@Override
	public ByteBuffer getByteBuffer(){
		return byteBuffer;
	}
	
	@Override
	public synchronized Tuple<M> create() throws IOException, ClassNotFoundException, BufferUnderflowException {
		MVTuple<M> r = null;
		synchronized(byteBuffer){		
			byteBuffer.flip();
			//logger.debug("create "+byteBuffer);
			Object[] attributes = new Object[dataHandler.length];
			for (int i=0;i<dataHandler.length;i++){
				try{
					attributes[i] = dataHandler[i].readData(byteBuffer);
				}catch(BufferUnderflowException e){
					System.out.println("BufferUnderflowException at attribute no " + i);
					e.printStackTrace();
				}
			}
			r = new MVTuple<M>(attributes);
			byteBuffer.clear();
		}
		return r;
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
	
	public void put(int pos, Object val){
		synchronized(byteBuffer){
			dataHandler[pos].writeData(byteBuffer, val);
		}
	}
	
	@Override
	public void put(Tuple<M> tuple) {
		if (tuple.size() != dataHandler.length){
			throw new IllegalArgumentException("Incompatible Relational Tuple");
		}
		synchronized(byteBuffer){
			
			int size = memSize(tuple);

			if (size > byteBuffer.capacity()) {
				byteBuffer = ByteBuffer.allocate(size * 2);
			}
			
			byteBuffer.clear();
			
			for (int i=0;i<dataHandler.length;i++){
				dataHandler[i].writeData(byteBuffer, tuple.getAttribute(i));
			}
			byteBuffer.flip();
		}
	}
	
	public int memSize(Object attribute) {
		Tuple<?> r = (Tuple<?>) attribute;
		int size = 0;
		for (int i = 0; i < dataHandler.length; i++) {
			size += dataHandler[i].memSize(r.getAttribute(i));
		}
		return size;
	}
	
	@Override
	public MVRelationalTupleObjectHandler<M> clone() {
		return new MVRelationalTupleObjectHandler<M>(this);
	}
//	@SuppressWarnings("unchecked")
//	public static void main(String[] args) throws IOException, ClassNotFoundException {
//		SDFSchema schema = new SDFSchema();
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
//		Tuple<IMetaAttribute> r = h.create();
//		System.out.println(r);
//		
//	}


}
