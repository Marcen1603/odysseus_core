package de.uniol.inf.is.odysseus.scars.base;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.PortUnreachableException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.IllegalBlockingModeException;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access.AbstractSensorAccessPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class JDVEAccessMVPO <M extends IProbability> extends AbstractSensorAccessPO<MVRelationalTuple<M>, M> {

	private JDVEData<M> data;
	private int port;
	protected MVRelationalTuple<M> buffer;
	private SDFAttributeList outputSchema;
	
	public JDVEAccessMVPO(int pPort) {
		this.port = pPort;
	}
	
	public JDVEAccessMVPO( JDVEAccessMVPO<M> po ) {
		this.port = po.port;
	}
	
	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		super.setOutputSchema(outputSchema);
		this.outputSchema = outputSchema;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		try {
			data = new JDVEData<M>(this.port, outputSchema);
		}
		catch (SocketException ex){
			throw new OpenFailedException(ex);
		}
	}
	
	@Override
	protected void process_done() {
		data.closeJDVEDataPort();
	}

	@Override
	public boolean hasNext() {
		if (buffer == null) {
			try {
				buffer = data.getScan();
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				return false;
			} catch (PortUnreachableException e) {
				e.printStackTrace();
				return false;
			} catch (IllegalBlockingModeException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public AbstractSource<MVRelationalTuple<M>> clone() {
		return new JDVEAccessMVPO<M>(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}

	@Override
	public void transferNext() {
		if( buffer != null ) {
			transfer(buffer);
			sendPunctuation(new PointInTime(data.getLastTimestamp()));
		}
		buffer = null;
	}
}

class JDVEData<M extends IProbability> {
	
	private static final int RECEIVE_SIZE = 10000;
	private static final int TIMEOUT = 10000;
		
	private int port = -1;
	private DatagramSocket clientSocket;
	private SDFAttributeList attributeList;
	private long lastTimestamp;

	public JDVEData(int pPort, SDFAttributeList list) throws SocketException {
		this.port = pPort;
		this.clientSocket = new DatagramSocket(this.port);
		this.clientSocket.setSoTimeout(TIMEOUT);
		this.attributeList = list;
	}
	
	public long getLastTimestamp() {
		return lastTimestamp;
	}
	
	@SuppressWarnings("unchecked")
	public MVRelationalTuple<M> getScan() throws SocketTimeoutException, PortUnreachableException, IllegalBlockingModeException, IOException {
		/* Ben�tigter Puffer:
		 * carType: 4 Byte
		 * carTrafficID: 4 Byte
		 * laneID: 4 Byte
		 * positionUTM: 8 Byte * 6 + 4 F�llbytes f�r das Array
		 * velocity: 4 Byte
		 * length: 4 Byte
		 * width: 4 Byte 
		 * = (76 Byte + 4 F�llbytes f�r das struct) * 50 Autos f�r einen Scan = 4000 Bytes */
		byte[] receiveData = new byte[RECEIVE_SIZE];
		
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		
		clientSocket.receive(receivePacket);
		
		/* ByteBuffer �bernimmt die Daten. ByteBuffer besitzt
	     * die Methoden, die wir zum Auslesen ben�tigen. */
		ByteBuffer byteBuffer = ByteBuffer.wrap(receiveData);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		Object res = parseStart(attributeList, byteBuffer);
		
		if( res instanceof MVRelationalTuple<?>) {
			return (MVRelationalTuple<M>)res;
		} else {
			MVRelationalTuple<M> tuple = new MVRelationalTuple<M>(1);
			tuple.setAttribute(0, res);
			return tuple;
		}
	}
	
	public MVRelationalTuple<M> parseStart(SDFAttributeList schema, ByteBuffer bb) {
		MVRelationalTuple<M> base = new MVRelationalTuple<M>(1);
		base.setAttribute(0, parseNext(schema.get(0), bb));
		return base;
	}
	
	public MVRelationalTuple<M> parseRecord(SDFAttribute schema, ByteBuffer bb) {
		int count = schema.getSubattributeCount();
		MVRelationalTuple<M> recordTuple = new MVRelationalTuple<M>(count);
		for( int i = 0; i < count; i++ ) {
			Object obj = parseNext(schema.getSubattribute(i), bb);
			recordTuple.setAttribute(i, obj);
		}
		return recordTuple;
	}
	
	public MVRelationalTuple<M> parseList(SDFAttribute schema, ByteBuffer bb) {
//		int count = bb.getInt(); // TODO: hier Länge aus Buffer einlesen
//		System.out.println(count);
		int count = 50;
		MVRelationalTuple<M> recordTuple = new MVRelationalTuple<M>(count);

		for( int i = 0; i < count; i++ ) {
			Object obj = parseNext(schema.getSubattribute(0), bb); 
			recordTuple.setAttribute(i, obj);
		}
		return recordTuple;
	}
		
	public Object parseAttribute(SDFAttribute schema, ByteBuffer bb) {
		if( "Integer".equals(schema.getDatatype().getURIWithoutQualName() )) {
			return bb.getInt();
		} else 	if( "Double".equals(schema.getDatatype().getURIWithoutQualName() )) {
			return bb.getDouble();
		} else 	if( "String".equals(schema.getDatatype().getURIWithoutQualName() )) {
			throw new RuntimeException("not implememted yet");			
		} else 	if( "Long".equals(schema.getDatatype().getURIWithoutQualName() )) {
			return bb.getLong();
		} else 	if( "Float".equals(schema.getDatatype().getURIWithoutQualName() )) {
			return bb.getFloat();
		} else if( "MV".equals(schema.getDatatype().getURIWithoutQualName() )) {
			return bb.getDouble();
		} else if( "MV Float".equals(schema.getDatatype().getURIWithoutQualName() )) {
			return bb.getFloat();
		} else if( "MV Long".equals(schema.getDatatype().getURIWithoutQualName() )) {
			return bb.getLong();
		} else if( "MV Integer".equals(schema.getDatatype().getURIWithoutQualName() )) {
			return bb.getInt();
//		} else 	if( "Date".equals(schema.getDatatype().getURIWithoutQualName() )) {
//			throw new RuntimeException("not implememted yet");
		} else if ("StartTimestamp".equals(schema.getDatatype()
				.getURIWithoutQualName())) {
			lastTimestamp = bb.getLong();
			return lastTimestamp;
		} else if ("EndTimestamp".equals(schema.getDatatype()
				.getURIWithoutQualName())) {
			return bb.getLong();
		} else {
			throw new RuntimeException("not implememted yet");			
		}
	}
	
	public static float arr2float (byte[] arr, int start) {
		int i = 0;
		int len = 4;
		int cnt = 0;
		byte[] tmp = new byte[len];
		for (i = start; i < (start + len); i++) {
			tmp[cnt] = arr[i];
			cnt++;
		}
		int accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 32; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return Float.intBitsToFloat(accum);
	}
	
	public Object parseNext(SDFAttribute attr, ByteBuffer bb) {
		Object obj = null;
		if( "Record".equals(attr.getDatatype().getURIWithoutQualName())) {
			obj = parseRecord(attr, bb);
		} else if( "List".equals(attr.getDatatype().getURIWithoutQualName())) {
			obj = parseList(attr, bb);
		} else {
			obj = parseAttribute(attr, bb);
		}
		return obj;
	}
	
	public void closeJDVEDataPort() {
		this.clientSocket.close();
	}
}