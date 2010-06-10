package de.uniol.inf.is.odysseus.scars.base.physicaloperator;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access.AbstractSensorAccessPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.PortUnreachableException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.IllegalBlockingModeException;
import java.util.ArrayList;

public abstract class JDVEDataInputStreamAccessMVPO <M extends IProbability> extends AbstractSensorAccessPO<MVRelationalTuple<M>, M> {

	private JDVEData data;
	private int port;
	protected ArrayList<CarData> buffer;
	
	public JDVEDataInputStreamAccessMVPO(int pPort) {
		this.port = pPort;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		try {
			data = new JDVEData(this.port);
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

class JDVEData {
	
	private int port = -1;
	private DatagramSocket clientSocket;
	private int numberOfCars;

	public JDVEData(int pPort) throws SocketException {
		this.numberOfCars = 50;
		this.port = pPort;
		this.clientSocket = new DatagramSocket(this.port);
		clientSocket.setSoTimeout(10000);
	}
	
	public ArrayList<CarData> getScan() throws SocketTimeoutException, PortUnreachableException, IllegalBlockingModeException, IOException {
		ArrayList<CarData> result = new ArrayList<CarData>();
		
		/* Benötigter Puffer:
		 * carType: 4 Byte
		 * carTrafficID: 4 Byte
		 * laneID: 4 Byte
		 * positionUTM: 8 Byte * 6 + 4 Füllbytes für das Array
		 * velocity: 4 Byte
		 * length: 4 Byte
		 * width: 4 Byte 
		 * = (76 Byte + 4 Füllbytes für das struct) * 50 Autos für einen Scan = 4000 Bytes */
		byte[] receiveData = new byte[4000];
		
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		
		clientSocket.receive(receivePacket);
		
		/* ByteBuffer übernimmt die Daten. ByteBuffer besitzt
	     * die Methoden, die wir zum Auslesen benötigen. */
		ByteBuffer bb = ByteBuffer.wrap(receiveData);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		
		/* Pro Scan werden (zumindest für den 
	     * vertikalen Prototypen)
	     * immer 50 Autos übermittelt */
	    for (int k = 0; k < this.numberOfCars; k++)
	    {
	    	CarData currentCar = new CarData();
	    	currentCar.setCarType(bb.getInt());
	    	currentCar.setCarTrafficID(bb.getInt());
	    	currentCar.setLaneID(bb.getInt());
	    	
	    	/* Füllbytes für das Array auslesen */
	        bb.getInt();
	        
	        double[] positionUTM = new double[6];
	        for (int j = 0; j < 6; j++)
	        {
	          positionUTM[j] = bb.getDouble();
	        }
	        currentCar.setPositionUTM(positionUTM);
	        currentCar.setVelocity(bb.getFloat());
	        currentCar.setLength(bb.getFloat());
	        currentCar.setWidth(bb.getFloat());
	        
	        /* Füllbytes für das Struct 'CarData' auslesen */
	        bb.getInt();
	        
	        /* Das Auto der ArrayList hinzufügen, 
	         * falls es existiert (carTrafficID != -1) */
	        if (currentCar.getCarTrafficID() != -1)
	        	result.add(currentCar);
	    }
	    return result;
	}
	
	private double[] readArray(ByteBuffer bb, int length) {
		/* Füllbytes für das Array auslesen */
	    bb.getInt();
	        
	    double[] result = new double[length];
	    for (int j = 0; j < length; j++)
	    {
	      result[j] = bb.getDouble();
		}
	    return result;
	}
	
	public void closeJDVEDataPort() {
		this.clientSocket.close();
	}
}