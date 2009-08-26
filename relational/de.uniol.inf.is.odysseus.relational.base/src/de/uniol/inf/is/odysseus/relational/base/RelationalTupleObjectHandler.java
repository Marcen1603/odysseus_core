package de.uniol.inf.is.odysseus.relational.base;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.DoubleHandler;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.IAtomicDataHandler;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.IObjectHandler;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.IntegerHandler;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.LongHandler;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.StringHandler;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class RelationalTupleObjectHandler<M extends IClone> implements
		IObjectHandler<RelationalTuple<M>> {

	private static final Logger logger = LoggerFactory.getLogger( RelationalTupleObjectHandler.class );
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
			} else if (uri.equals("Long")) {
				this.dataHandler[i++] = new LongHandler();
			} else if (uri.equals("Double")) {
				this.dataHandler[i++] = new DoubleHandler();
			} else if (uri.equals("String")) {
				this.dataHandler[i++] = new StringHandler();
			} else {
				throw new RuntimeException("illegal datatype");
			}
		}
	}


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
			logger.warn("RelationalTupleObjectHandler OVERFLOW");
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
		ByteBuffer buffer = h.getByteBuffer();
		RelationalTuple<IClone> r = h.create();
		System.out.println(r);
		
	}


}
