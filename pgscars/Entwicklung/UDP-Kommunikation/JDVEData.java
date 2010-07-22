import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;


public class JDVEData {
	
	private int port = -1;

	public JDVEData(int pPort) {
		this.port = pPort;
	}
	
	public ArrayList<CarData> getScan() throws IOException {
		ArrayList<CarData> result = new ArrayList<CarData>();
		DatagramSocket clientSocket = new DatagramSocket(this.port);
		
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
	    for (int k = 0; k < 50; k++)
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
}
