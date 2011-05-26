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
import de.uniol.inf.is.odysseus.physicaloperator.access.DoubleHandler;
import de.uniol.inf.is.odysseus.physicaloperator.access.IAtomicDataHandler;
import de.uniol.inf.is.odysseus.physicaloperator.access.IObjectHandler;
import de.uniol.inf.is.odysseus.physicaloperator.access.IntegerHandler;
import de.uniol.inf.is.odysseus.physicaloperator.access.LongHandler;
import de.uniol.inf.is.odysseus.physicaloperator.access.StringHandler;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class RelationalTupleObjectHandler<M extends IMetaAttribute> implements
		IObjectHandler<RelationalTuple<M>> {

	//private static final Logger logger = LoggerFactory.getLogger( RelationalTupleObjectHandler.class );
	ByteBuffer byteBuffer = null;
	private IAtomicDataHandler[] dataHandler;
		
	public RelationalTupleObjectHandler(SDFAttributeList schema) {
		byteBuffer = ByteBuffer.allocate(1024);
		createDataReader(schema);		
	}
	
	public RelationalTupleObjectHandler(
			RelationalTupleObjectHandler<M> relationalTupleObjectHandler) {
		super();
		if (relationalTupleObjectHandler.dataHandler != null){
			int l = relationalTupleObjectHandler.dataHandler.length;
			dataHandler = new IAtomicDataHandler[l];
			System.arraycopy(relationalTupleObjectHandler.dataHandler, 0, dataHandler, 0, l);
		}
	}

	private void createDataReader(SDFAttributeList schema) {
		this.dataHandler = new IAtomicDataHandler[schema.size()];
		int i = 0;
		for (SDFAttribute attribute : schema) {
			String uri = attribute.getDatatype().getURI(false);
			if (uri.equals("Integer")) {
				this.dataHandler[i++] = new IntegerHandler();
			} else if (uri.equals("Long") || uri.endsWith("Timestamp")) {
				this.dataHandler[i++] = new LongHandler();
			} else if (uri.equals("Double")) {
				this.dataHandler[i++] = new DoubleHandler();
			} else if (uri.equals("String")) {
				this.dataHandler[i++] = new StringHandler();
			} else if (uri.equalsIgnoreCase("SpatialPoint") ||
					uri.equalsIgnoreCase("SpatialLine") ||
					uri.equalsIgnoreCase("SpatialPolygon") || 
					uri.equalsIgnoreCase("SpatialMulitPoint") ||
					uri.equalsIgnoreCase("SpatialMultiLine") ||
					uri.equalsIgnoreCase("SpatialMultiPolygon")){
				
				try {
					Class clazz = Class.forName("de.uniol.inf.is.odysseus.spatial.access.SpatialByteHandler");
					this.dataHandler[i++] = (IAtomicDataHandler)clazz.newInstance();
				} catch (InstantiationException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			} else {
				throw new RuntimeException("illegal datatype "+uri);
			}
		}
	}


	@Override
	public ByteBuffer getByteBuffer(){
		return byteBuffer;
	}
	
	@Override
	public synchronized RelationalTuple<M> create() throws IOException, ClassNotFoundException, BufferUnderflowException {
		RelationalTuple<M> r = null;
		synchronized(byteBuffer){		
			byteBuffer.flip();
			//logger.debug("create "+byteBuffer);
			Object[] attributes = new Object[dataHandler.length];
			for (int i=0;i<dataHandler.length;i++){
				attributes[i] = dataHandler[i].readData(byteBuffer);
			}
			r = new RelationalTuple<M>(attributes);
			byteBuffer.clear();
		}
		return r;
	}

	private void checkOverflow(ByteBuffer buffer, int size) {
		if (size+byteBuffer.position()>=byteBuffer.capacity()){
			// TODO: Effizientere �berlaufbehandlung?
			//logger.warn("RelationalTupleObjectHandler OVERFLOW");
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
	public void put(RelationalTuple<M> relationalTuple) {
		if (relationalTuple.getAttributeCount() != dataHandler.length){
			throw new IllegalArgumentException("Incompatible Relational Tuple");
		}
		synchronized(byteBuffer){
			
			if (relationalTuple.memSize(true) > byteBuffer.capacity()){
				byteBuffer = ByteBuffer.allocate(relationalTuple.memSize(false)*2);	
			}
			byteBuffer.clear();
			
			for (int i=0;i<dataHandler.length;i++){
				dataHandler[i].writeData(byteBuffer, relationalTuple.getAttribute(i));
			}
			byteBuffer.flip();
		}
	}
	
	@Override
	public RelationalTupleObjectHandler<M> clone() {
		return new RelationalTupleObjectHandler<M>(this);
	}
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		SDFAttributeList schema = new SDFAttributeList();
		SDFAttribute a = new SDFAttribute("a_int");
		a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
		schema.add(a);
		a = new SDFAttribute("a_long");
		a.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		schema.add(a);
		a = new SDFAttribute("a_double");
		a.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
		schema.add(a);
		a = new SDFAttribute("a_String");
		a.setDatatype(SDFDatatypeFactory.getDatatype("String"));
		schema.add(a);
		RelationalTupleObjectHandler h = new RelationalTupleObjectHandler(schema);
		h.put(0,10);
		h.put(1,100l);
		h.put(2,100.0d);
		h.put(3,"Hallo Folks");
			
		h.getByteBuffer();
		RelationalTuple<IMetaAttribute> r = h.create();
		System.out.println(r);
		
	}


}
