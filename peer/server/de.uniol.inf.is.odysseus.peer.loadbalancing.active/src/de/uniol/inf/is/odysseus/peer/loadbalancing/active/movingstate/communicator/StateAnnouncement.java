package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator;

import java.io.Serializable;

/**
 * State announcement needed for state transmission. Contains needed
 * informations about transmission (number of messages, checksum etc)
 * 
 * @author ChrisToenjesDeye
 *
 */
public class StateAnnouncement implements Serializable {
	private static final long serialVersionUID = -4044466481622742090L;

	// maximum size of messages 89kb
	public static final int MAX_MESSAGE_SIZE = 900 * 1024;

	private int arrayLenght;
	private int numberOfMessages;
	private long crcChecksum;

	public StateAnnouncement(int arrayLenght, long crcChecksum) {
		this.arrayLenght = arrayLenght;
		this.crcChecksum = crcChecksum;

		// calculate number of packets
		numberOfMessages = (arrayLenght / MAX_MESSAGE_SIZE);
		if (arrayLenght % MAX_MESSAGE_SIZE != 0) {
			// If there is a rest, we need an extra packet for this
			numberOfMessages++;
		}
	}

	public int getArrayLenght() {
		return arrayLenght;
	}

	public int getNumberOfMessages() {
		return numberOfMessages;
	}

	public long getChecksum() {
		return crcChecksum;
	}

}
