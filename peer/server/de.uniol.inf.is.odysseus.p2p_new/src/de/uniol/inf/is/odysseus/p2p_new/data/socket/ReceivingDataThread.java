package de.uniol.inf.is.odysseus.p2p_new.data.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.p2p_new.activator.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.util.ObjectByteConverter;

public class ReceivingDataThread extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(ReceivingDataThread.class);

	private final InetAddress address;
	private final int port;
	private final IReceivingDataThreadListener listener;

	private Socket socket;
	private boolean isRunning;

	public ReceivingDataThread(InetAddress address, int port, IReceivingDataThreadListener listener) {
		this.address = address;
		this.port = port;
		this.listener = listener;
		
		setDaemon(true);
		setName("Receiving thread from " + address + ":" + port);
	}

	@Override
	public void run() {
		byte[] buffer = new byte[P2PNewPlugIn.TRANSPORT_BUFFER_SIZE];
		MessageByteBuffer mb = new MessageByteBuffer();
		
		try {
			socket = new Socket(address, port);
			LOG.debug("Opened socket on port {} for address {}", port, address);
			InputStream inputStream = socket.getInputStream();
			isRunning = true;

			while (isRunning) {
				int bytesRead = inputStream.read(buffer);
				if (bytesRead == -1) {
					LOG.debug("Reached end of data stream. Socket closed...");
					break;
				} else if (bytesRead > 0) {
					byte[] msg = new byte[bytesRead];
					System.arraycopy(buffer, 0, msg, 0, bytesRead);

					mb.put(msg);

					List<byte[]> packets = mb.getPackets();
					for (byte[] packet : packets) {
						processBytes(packet);
					}
				}
			}
		} catch (SocketException e) {
			tryCloseSocket(socket);
			if (!e.getMessage().equals("socket closed")) {
				LOG.error("Exception while reading socket data", e);
			}

		} catch (IOException e) {
			LOG.error("Exception while reading socket data", e);

			tryCloseSocket(socket);
		} finally {
			tryCloseSocket(socket);
			
			socket = null;
			buffer = null;
			mb = null;
			
			listener.onFinish();
		}
	}

	private void processBytes(byte[] msg) {
		byte[] realMsg = new byte[msg.length - 1];
		System.arraycopy(msg, 1, realMsg, 0, realMsg.length);

		// System.err.println("Received packages " + (packageCount++));

		byte flag = msg[0];
		if (flag == 0) {
			// data
			listener.onReceivingData(realMsg);
		} else if (flag == 1) {
			IPunctuation punc = (IPunctuation) ObjectByteConverter.bytesToObject(realMsg);
			listener.onReceivingPunctuation(punc);
		} else if (flag == 2) {
			listener.onReceivingDoneEvent();
		} else {
			LOG.error("Unknown flag {}", flag);
		}
	}

	public void stopRunning() {
		isRunning = false;
		tryCloseSocket(socket);
	}

	private static void tryCloseSocket(Socket socket) {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e1) {
		}
	}
}
