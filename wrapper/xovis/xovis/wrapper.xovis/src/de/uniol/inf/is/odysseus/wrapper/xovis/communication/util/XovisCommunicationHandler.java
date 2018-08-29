package de.uniol.inf.is.odysseus.wrapper.xovis.communication.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.xovis.physicaloperator.access.XovisTransportHandler.XOVISSTREAMTYPE;


/**
 * 
 * This communication handler establishes a TCP socket client connection to a
 * xovis stereo camera. It establishes the connection by sending a connection
 * request. To stay connected, it sends a heart beat signal every 7 seconds. If
 * no heartbead is sent in 10 seconds, the connection is closed on the server
 * side.
 * 
 * @author Jan Benno Meyer zu Holte
 *
 */
public class XovisCommunicationHandler implements Runnable {
	
	protected final Logger LOG = LoggerFactory
			.getLogger(XovisCommunicationHandler.class);

	protected Thread t;

	/* * Specify the sensors IP and port here. */
	private String host = "192.168.1.168";
	private int port = 49156;
	private Socket tCPClientSocket;

	private InetAddress address;
	private DatagramSocket uDPClientSocket;

	private TCPStreamEstablisher tCPStreamEstablisher;
	private UDPStreamEstablisher uDPStreamEstablisher;
	
	private XOVISSTREAMTYPE type;

	public XovisCommunicationHandler(String add, int port, XOVISSTREAMTYPE type) {
		this.host = add;
		this.port = port;
		this.type = type;
		init();
		
	}

	private void init() {
		try {
			switch (this.type) {
			case EVENTSTREAM:
				tCPClientSocket = new Socket(this.host, this.port);
				break;
			case OBJECTSTREAM:
				this.address = InetAddress.getByName(host);
				this.setuDPClientSocket(new DatagramSocket());
				break;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.setName("XovisCommunicationHandler");
			t.start();
			LOG.debug(this.getClass().toString() + " started.");
		} else {
			LOG.debug("Trying to start Xovis-Client - but it was already running");
		}
	}

	public void connect() {
		switch (this.type) {
		case EVENTSTREAM:
			try {
				tCPStreamEstablisher = new TCPStreamEstablisher(tCPClientSocket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			tCPStreamEstablisher.start();
			break;
		case OBJECTSTREAM:
			uDPStreamEstablisher = new UDPStreamEstablisher(this.uDPClientSocket, this.address, this.port);
			uDPStreamEstablisher.start();
			break;
		}
	}

	public InputStream getInputStream() throws IOException {
		if (tCPClientSocket != null) {
			return tCPClientSocket.getInputStream();
		} 
		return null;
	}

	public void close() {
		switch (this.type) {
		case EVENTSTREAM:
			tCPStreamEstablisher.close();
			try {
				tCPClientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case OBJECTSTREAM:
			uDPStreamEstablisher.close();
			uDPClientSocket.close();
			break;
		}
		
		if (t != null) {
			t.interrupt();
		}
		t = null;
		LOG.debug(this.getClass().toString() + " closed.");
	}

	public Thread getT() {
		return t;
	}

	public void setT(Thread t) {
		this.t = t;
	}

	@Override
	public void run() {

	}

	public DatagramSocket getuDPClientSocket() {
		return uDPClientSocket;
	}

	private void setuDPClientSocket(DatagramSocket uDPClientSocket) {
		this.uDPClientSocket = uDPClientSocket;
	}
}
