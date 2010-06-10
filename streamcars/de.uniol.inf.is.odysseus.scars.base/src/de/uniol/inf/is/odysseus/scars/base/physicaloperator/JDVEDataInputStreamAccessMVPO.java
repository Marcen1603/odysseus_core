package de.uniol.inf.is.odysseus.scars.base.physicaloperator;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access.AbstractSensorAccessPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
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

public class JDVEDataInputStreamAccessMVPO <M extends IProbability> extends AbstractSensorAccessPO<MVRelationalTuple<M>, M> {

	@Override
	public SDFAttributeList getOutputSchema() {
		return null;
	}

	@Override
	protected void process_open() throws OpenFailedException {
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public void transferNext() {
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
	
	public ArrayList<CarData> getScan() {
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
		
		try {
			clientSocket.receive(receivePacket);
		}
		catch (SocketTimeoutException ex) {
			//TODO: Abfangen und Loggen
			System.err.println(ex.getMessage());
		}
		catch (PortUnreachableException ex) {
			//TODO: Abfangen und Loggen
			System.err.println(ex.getMessage());
		}
		catch (IllegalBlockingModeException ex) {
			//TODO: Abfangen und Loggen
			System.err.println(ex.getMessage());
		}
		catch (IOException ex) {
			//TODO: Abfangen und Loggen
			System.err.println(ex.getMessage());
		}
		
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
}

class CarData {

	private int carType;
	private int carTrafficID;
	private int laneID;
	private double[] positionUTM;
	private float velocity;
	private float length;
	private float width;
	
	public CarData() {}
	
	public CarData(int pCarType, int pCarTrafficID, int pLaneID, double[] pPositionUTM, float pVelocity, float pLength, float pWidth) {
		this.setCarType(pCarType);
		this.setCarTrafficID(pCarTrafficID);
		this.setLaneID(pLaneID);
		this.setPositionUTM(pPositionUTM);
		this.setVelocity(pVelocity);
		this.setLength(pLength);
		this.setWidth(pWidth);
	}

	/**
	 * @param Setzt den carType auf carType.
	 */
	public void setCarType(int carType) {
		this.carType = carType;
	}

	/**
	 * @return carType
	 */
	public int getCarType() {
		return carType;
	}

	/**
	 * @param carTrafficID the carTrafficID to set
	 */
	public void setCarTrafficID(int carTrafficID) {
		this.carTrafficID = carTrafficID;
	}

	/**
	 * @return the carTrafficID
	 */
	public int getCarTrafficID() {
		return carTrafficID;
	}

	/**
	 * @param laneID the laneID to set
	 */
	public void setLaneID(int laneID) {
		this.laneID = laneID;
	}

	/**
	 * @return the laneID
	 */
	public int getLaneID() {
		return laneID;
	}

	/**
	 * @param positionUTM the positionUTM to set
	 */
	public void setPositionUTM(double[] positionUTM) {
		this.positionUTM = positionUTM;
	}

	/**
	 * @return the positionUTM
	 */
	public double[] getPositionUTM() {
		return positionUTM;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	/**
	 * @return the velocity
	 */
	public float getVelocity() {
		return velocity;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(float length) {
		this.length = length;
	}

	/**
	 * @return the length
	 */
	public float getLength() {
		return length;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}
	
}