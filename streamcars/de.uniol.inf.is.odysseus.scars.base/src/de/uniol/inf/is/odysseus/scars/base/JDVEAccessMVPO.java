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
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access.AbstractSensorAccessPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class JDVEAccessMVPO <M extends IProbability> extends AbstractSensorAccessPO<MVRelationalTuple<M>, M> {

	private JDVEData<M> data;
	private int port;
	protected MVRelationalTuple<M> buffer;
	
	public JDVEAccessMVPO(int pPort) {
		this.port = pPort;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		try {
			data = new JDVEData<M>(this.port, getOutputSchema());
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
		/* Lassen wir so, weil wir das erstmal nicht behandeln */
		return false;
	}

	@Override
	public AbstractSource<MVRelationalTuple<M>> clone() {
		// TODO Auto-generated method stub
		return null;
	}
}

class JDVEData<M extends IProbability> {
	
	private static final int RECEIVE_SIZE = 10000;
	private static final int TIMEOUT = 10000;
		
	private int port = -1;
	private DatagramSocket clientSocket;
	private SDFAttributeList attributeList;

	public JDVEData(int pPort, SDFAttributeList list) throws SocketException {
		this.port = pPort;
		this.clientSocket = new DatagramSocket(this.port);
		this.clientSocket.setSoTimeout(TIMEOUT);
		this.attributeList = list;
	}
	
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
		ByteBuffer bb = ByteBuffer.wrap(receiveData);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return parseStart(attributeList.get(0), bb);
	}
	
	public MVRelationalTuple<M> parseStart(SDFAttribute schema, ByteBuffer bb) {
		MVRelationalTuple<M> base = new MVRelationalTuple<M>(1);
		base.addAttributeValue(0, parseNext(schema.getSubattribute(0), bb));
		return base;
	}
	
	public MVRelationalTuple<M> parseRecord(SDFAttribute schema, ByteBuffer bb) {
		int count = schema.getSubattributeCount();
		MVRelationalTuple<M> recordTuple = new MVRelationalTuple<M>(count);
		for( int i = 0; i < count; i++ ) {
			Object obj = parseNext(schema.getSubattribute(i), bb);
			recordTuple.addAttributeValue(i, obj);
		}
		return recordTuple;
	}
	
	public MVRelationalTuple<M> parseList(SDFAttribute schema, ByteBuffer bb) {
		int count = bb.getInt(); // TODO: hier Länge aus Buffer einlesen
		MVRelationalTuple<M> recordTuple = new MVRelationalTuple<M>(count);

		for( int i = 0; i < count; i++ ) {
			Object obj = parseNext(schema.getSubattribute(i), bb);
			recordTuple.addAttributeValue(i, obj);
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
//		} else 	if( "Date".equals(schema.getDatatype().getURIWithoutQualName() )) {
//			throw new RuntimeException("not implememted yet");
		} else {
			throw new RuntimeException("not implememted yet");			
		}
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