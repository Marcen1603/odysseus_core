import java.io.IOException;
import java.util.ArrayList;


public class UDPClientCars
{
  public static void main(String[] args) throws IOException
  {
	  JDVEData jdve = new JDVEData(5001);
	  
	  ArrayList<CarData> currentScan = jdve.getScan();
	  
	  /* Ausgabe */
	  for (int i=0; i<50; i++) {
		  System.out.println("CarTrafficID: " + currentScan.get(i).getCarTrafficID());
		  System.out.println("CarType: " + currentScan.get(i).getCarType());
		  System.out.println("LaneID: " + currentScan.get(i).getLaneID());
		  System.out.println("Length: " + currentScan.get(i).getLength());
		  System.out.println("Velocity: " + currentScan.get(i).getVelocity());
		  System.out.println("Width: " + currentScan.get(i).getWidth());
		  for (int j=0;j<6;j++) {
			  System.out.println("PositionUTM[" + j + "]: " + currentScan.get(i).getPositionUTM()[j]);
		  }
	  }
	  
	  
//	/* Steuert per UDP den Port 5001 an*/
//    DatagramSocket clientSocket = new DatagramSocket(5001);
//    
//    /* Puffer für ankommende Daten. Leider ist nicht so leicht
//     * nachvollziehbar an welchen Stellen Füllbytes eingefügt werden. */
//    byte[] receiveData = new byte[(8+(32+32+32+(4+64*6)+32+32+32+28)*100)/8];
//    
//    /* Ein UDP-Paket mit dem Puffer receiveData */
//    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//    
//    /* Die Anzahl der Scans die wir empfangen wollen.
//     * Momentan noch begrenzt. Irgendwann while(true)-Schleife */
//    for (int i = 0; i < 2; ++i)
//    {
//      System.out.println();
//      System.out.println("Scan " + i);
//      System.out.println("*************************");
//      System.out.println();
//      
//      /* Empfange Daten! Wird erst beendet,
//       * wenn auch Daten angekommen sind. */
//      clientSocket.receive(receivePacket);
//      
//      /* ByteBuffer übernimmt die Daten. ByteBuffer besitzt
//       * die Methoden, die wir zum Auslesen benötigen. */
//      ByteBuffer bb = ByteBuffer.wrap(receiveData);
//      
//      /* Byte-Order auf Little-Endian setzen. */
//      bb.order(ByteOrder.LITTLE_ENDIAN);
//      
//      /* Pro Scan werden (zumindest für den 
//       * vertikalen Prototypen)
//       * immer 50 Autos übermittelt */
//      for (int k = 0; k < 2; ++k)
//      {
//        System.out.println();
//        System.out.println("Auto " + k);
//        System.out.println();
//        
//        int type = bb.getInt();
//        int carTrafficID = bb.getInt();
//        //bb.getInt();
//        int laneID = bb.getInt();
//        double[] positionUTM = new double[6];
//        
//        /* Füllbytes für das Array auslesen */
//        bb.getInt();
//        
//        for (int j = 0; j < 6; ++j)
//        {
//          positionUTM[j] = bb.getDouble();
//        }
//        float velocity = bb.getFloat();
//        float length = bb.getFloat();
//        float width = bb.getFloat();
//        
//        /* Ausgabe: */
//        System.out.println("Typ: " + type);
//        System.out.println("CarTrafficID: " + carTrafficID);
//        System.out.println("LaneID: " + laneID);
//        for (int j = 0; j < 6; ++j)
//        {
//          System.out.println("PositionUTM: " + positionUTM[j]);
//        }
//        System.out.println("Velocity: " + velocity);
//        System.out.println("Length: " + length);
//        System.out.println("Width: " + width);
//        System.out.println(bb.getInt());
//      }
//    }
  }
}
