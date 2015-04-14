package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException;
import de.uniol.inf.is.odysseus.peer.transmission.DataTransmissionException;
import de.uniol.inf.is.odysseus.peer.transmission.DataTransmissionManager;
import de.uniol.inf.is.odysseus.peer.transmission.ITransmissionSender;

/****
 * Implements a sender for serialized statefulPO states.
 * 
 * @author Carsten Cordes
 *
 */
public class MovingStateSender {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MovingStateSender.class);

	/***
	 * Transmission Sender
	 */
	private final ITransmissionSender transmission;

	/**
	 * Flag that is set when transmission is successful
	 */
	private boolean successfullyTransmitted = false;

	/***
	 * sets transmission successful Flag.
	 */
	public void setSuccessfullyTransmitted() {
		successfullyTransmitted = true;
	}

	/**
	 * 
	 * @return True if state was already successfully transmitted.
	 */
	public boolean isSuccessfullyTransmitted() {
		return successfullyTransmitted;
	}

	/***
	 * Constructor
	 * 
	 * @param peerID
	 *            PeerID to correspond with.
	 * @param pipeID
	 *            PipeID which should be used for transmission
	 * @throws DataTransmissionException
	 */
	public MovingStateSender(String peerID, String pipeID) throws DataTransmissionException {
		this.transmission = DataTransmissionManager.registerTransmissionSender(peerID, pipeID);
		this.transmission.open();
	}

	/**
	 * Sends a serializable data
	 * 
	 * @param toSend
	 *            Serializable data to send.
	 * @throws LoadBalancingException
	 */
	public void sendData(Serializable toSend) throws LoadBalancingException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(toSend);
			byte[] stateBytes = bos.toByteArray();

			// create CRC Checksum
			Checksum checksum = new CRC32();
			checksum.update(stateBytes, 0, stateBytes.length);
			long checksumValue = checksum.getValue();

			LOG.debug("Checksum created..");

			// create announcement
			StateAnnouncement announcement = new StateAnnouncement(stateBytes.length, checksumValue);
			byte[] stateAnnouncementBytes = convertAnnouncementToBytes(announcement);
			if (stateAnnouncementBytes != null) {
				// Send announcement (number of messages)
				transmission.sendData(stateAnnouncementBytes);
				LOG.debug("Annoucement sent.");

				if (stateBytes.length > StateAnnouncement.MAX_MESSAGE_SIZE) {
					// if we have more than one message
					LOG.error(String.format("State Transmission: Message with an size of %s splitted into %d parts with a size of %s. Start sending.", readableFileSize(announcement.getArrayLenght()), announcement.getNumberOfMessages(), readableFileSize(StateAnnouncement.MAX_MESSAGE_SIZE)));
					for (int i = 0; i < announcement.getNumberOfMessages(); i++) {
						// copy part of the array needed for message
						byte[] tempArray = null;

						if (i < announcement.getNumberOfMessages() - 1) {
							tempArray = Arrays.copyOfRange(stateBytes, i * StateAnnouncement.MAX_MESSAGE_SIZE, ((i + 1) * StateAnnouncement.MAX_MESSAGE_SIZE));
						} else {
							// last message
							tempArray = Arrays.copyOfRange(stateBytes, i * StateAnnouncement.MAX_MESSAGE_SIZE, announcement.getArrayLenght());
						}
						transmission.sendData(tempArray);
						LOG.info(String.format("State Transmission: Message %d if %d sent", i + 1, announcement.getNumberOfMessages()));
					}
				} else {
					transmission.sendData(stateBytes);
					LOG.debug("State sent (sender side should be ok.)");
				}

				Thread flushBufferThread = new Thread(new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i < 5; i++) {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
							}
							LOG.debug("Flushing Buffers.");
							flushBuffersIfStuck();
						}
					}
				});
				flushBufferThread.setName("Flush buffer Thread");
				flushBufferThread.setDaemon(true);
				flushBufferThread.start();

			}

		} catch (DataTransmissionException e) {
			LOG.error("State Transmission: Could not send Data.");
			e.printStackTrace();
			throw new LoadBalancingException("Could not send Data.");
		} catch (IOException e) {
			LOG.error("State Transmission: Could not serialize Data to Bytes.");
			e.printStackTrace();
			throw new LoadBalancingException("Could not serialize Data.");
		} catch (Exception e) {
			LOG.error("Some exception happened with state from {} ", toSend, e);
			throw new LoadBalancingException("Could not send State.");
		}

		finally {
			try {
				if (out != null) {
					out.close();
				}
				bos.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}
	}

	/**
	 * prints an readable file size
	 * 
	 * @param size
	 *            in bytes
	 * @return readable string
	 */
	public String readableFileSize(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	private byte[] convertAnnouncementToBytes(StateAnnouncement announcement) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(announcement);
			byte[] rawBytes = bos.toByteArray();
			return rawBytes;
		} catch (IOException e) {
			LOG.error("State Transmission: Could not serialize StateAnnouncement to bytes");
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				bos.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}
	}

	public synchronized void flushBuffersIfStuck() {
		transmission.flushBuffers();
	}

}
