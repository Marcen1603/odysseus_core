package de.uniol.inf.is.odysseus.wrapper.xovis.communication.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.protobuf.GeneratedMessageLite;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.xovis.communication.gpbObjects.TrackingObj.ObjectPositions;
import de.uniol.inf.is.odysseus.wrapper.xovis.communication.gpbObjects.TrackingObj.TrackedObject;

public class XovisObjectStreamListener extends AXovisStreamListener {

	public XovisObjectStreamListener(DatagramSocket uDPClientSocket,
			ArrayList<? extends GeneratedMessageLite> objList) {
		super(uDPClientSocket, objList);
	}

	@Override
	public void parseStream() {
		/*
		 * Receive the sensor stream continuously. Here the tcp packets are
		 * received and parsed to objects.
		 */
		// buffer for the receiving datagram
		byte[] receiveData = new byte[65536];
		try {
			DatagramPacket receiveDatagram = new DatagramPacket(receiveData, receiveData.length);
			uDPClientSocket.receive(receiveDatagram);
			// copy the received data to a byte array
			byte[] payload = Arrays.copyOf(receiveDatagram.getData(), receiveDatagram.getLength());
			// parse the received byte data to object positions
			ObjectPositions positions = ObjectPositions.parseFrom(payload);

			// process the received object positions (print them)
			// System.out.println("Received " + positions.getObjectCount()
			// + " object positions at "
			// + positions.getTimeStampMilliseconds()
			// + ", status is: " + positions.getStatus());
			if (positions.getObjectCount() > 0) {
				// System.out.println("Object positions:");
				for (TrackedObject p : positions.getObjectList()) {

					KeyValueObject<IMetaAttribute> out = KeyValueObject.createInstance();
					out.setAttribute("timestamp",
							positions.getTimeStampMilliseconds());
					out.setAttribute("status", positions.getStatus());
					out.setAttribute("numOfObjects", positions.getObjectCount());
					out.setAttribute("objectID", p.getId());
					out.setAttribute("posX", p.getX());
					out.setAttribute("posY", p.getY());
					getkVPairs().add(out);
					// System.out.println("\t Object [" + p.getId()
					// + "] is located at " + p.getX() + " / "
					// + p.getY());
				}
			}
			// leave the while loop if stream is not intact
			// if (!positions.getStatus().equals(ObjectPositions.Status.OK)) {
			// break;
			// }
		} catch (IOException e) {
			e.printStackTrace();
		}
		// }
	}
}
