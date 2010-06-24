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

import java.util.*;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access.AbstractSensorAccessPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DummyAccessMVPO <M extends IProbability> extends AbstractSensorAccessPO<MVRelationalTuple<M>, M> {

	private DummyJDVEData<M> data;
	private int port;
	protected MVRelationalTuple<M> buffer;
	private SDFAttributeList outputSchema;
	
	public DummyAccessMVPO(int pPort) {
		this.port = pPort;
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		try {
			data = new DummyJDVEData<M>(this.port, outputSchema);
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
		return null;
	}
	
	@Override
	public void transferNext() {
		if( buffer != null )
			transfer(buffer);
		buffer = null;
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		super.setOutputSchema(outputSchema);
		this.outputSchema = outputSchema;
	}
}

class DummyJDVEData<M extends IProbability> {
	
	private static final int RECEIVE_SIZE = 10000;
	private static final int TIMEOUT = 10000;
		
	private static Random rdm = new Random();
	private int port = -1;
	private DatagramSocket clientSocket;
	private SDFAttributeList attributeList;

	public DummyJDVEData(int pPort, SDFAttributeList list) throws SocketException {
		this.port = pPort;
		this.clientSocket = new DatagramSocket(this.port);
		this.clientSocket.setSoTimeout(TIMEOUT);
		this.attributeList = list;
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
		//byte[] receiveData = new byte[RECEIVE_SIZE];
		
		//DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		
		//clientSocket.receive(receivePacket);
		
		/* ByteBuffer �bernimmt die Daten. ByteBuffer besitzt
	     * die Methoden, die wir zum Auslesen ben�tigen. */
		//ByteBuffer byteBuffer = ByteBuffer.wrap(receiveData);
		//byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		Object res = parseNext(attributeList.get(0));
		
		if( res instanceof MVRelationalTuple<?>) {
			return (MVRelationalTuple<M>)res;
		} else {
			MVRelationalTuple<M> tuple = new MVRelationalTuple<M>(1);
			tuple.addAttributeValue(0, res);
			return tuple;
		}
	}
	
	public MVRelationalTuple<M> parseStart(SDFAttribute schema) {
		MVRelationalTuple<M> base = new MVRelationalTuple<M>(1);
		base.addAttributeValue(0, parseNext(schema.getSubattribute(0)));
		return base;
	}
	
	public MVRelationalTuple<M> parseRecord(SDFAttribute schema) {
		int count = schema.getSubattributeCount();
		MVRelationalTuple<M> recordTuple = new MVRelationalTuple<M>(count);
		for( int i = 0; i < count; i++ ) {
			Object obj = parseNext(schema.getSubattribute(i));
			recordTuple.addAttributeValue(i, obj);
		}
		return recordTuple;
	}
	
	public MVRelationalTuple<M> parseList(SDFAttribute schema) {
		int count; // TODO: hier Länge aus Buffer einlesen
		//System.out.println(count);
		count = 50;
		MVRelationalTuple<M> recordTuple = new MVRelationalTuple<M>(count);

		for( int i = 0; i < count; i++ ) {
			Object obj = parseNext(schema.getSubattribute(0));
			recordTuple.addAttributeValue(i, obj);
		}
		return recordTuple;
	}
		
	public Object parseAttribute(SDFAttribute schema) {
		if( "Integer".equals(schema.getDatatype().getURIWithoutQualName() )) {
			return rdm.nextInt();
		} else 	if( "Double".equals(schema.getDatatype().getURIWithoutQualName() )) {
			return rdm.nextDouble();
		} else 	if( "String".equals(schema.getDatatype().getURIWithoutQualName() )) {
			throw new RuntimeException("not implememted yet");			
		} else 	if( "Long".equals(schema.getDatatype().getURIWithoutQualName() )) {
			return rdm.nextLong();
		} else 	if( "Float".equals(schema.getDatatype().getURIWithoutQualName() )) {
			return rdm.nextFloat();
//		} else 	if( "Date".equals(schema.getDatatype().getURIWithoutQualName() )) {
//			throw new RuntimeException("not implememted yet");
		} else {
			throw new RuntimeException("not implememted yet");			
		}
	}
	
	public Object parseNext(SDFAttribute attr) {
		Object obj = null;
		if( "Record".equals(attr.getDatatype().getURIWithoutQualName())) {
			obj = parseRecord(attr);
		} else if( "List".equals(attr.getDatatype().getURIWithoutQualName())) {
			obj = parseList(attr);
		} else {
			obj = parseAttribute(attr);
		}
		return obj;
	}
	
	public void closeJDVEDataPort() {
		this.clientSocket.close();
	}
}