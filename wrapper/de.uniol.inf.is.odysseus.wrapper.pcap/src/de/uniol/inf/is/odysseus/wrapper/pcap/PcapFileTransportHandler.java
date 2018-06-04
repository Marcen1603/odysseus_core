package de.uniol.inf.is.odysseus.wrapper.pcap;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapClosedException;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * The Pcap file transport handler uses jNetPcap to read pcap files. <br />
 * <br />
 * In the field of computer network administration, pcap (packet capture)
 * consists of an application programming interface (API) for capturing network
 * traffic. Unix-like systems implement pcap in the libpcap library; Windows
 * uses a port of libpcap known as WinPcap. Monitoring software may use libpcap
 * and/or WinPcap to capture packets travelling over a network and, in newer
 * versions, to transmit packets on a network at the link layer, as well as to
 * get a list of network interfaces for possible use with libpcap or WinPcap.
 * The pcap API is written in C, so other languages such as Java, .NET
 * languages, and scripting languages generally use a wrapper; no such wrappers
 * are provided by libpcap or WinPcap itself. C++ programs may link directly to
 * the C API or use an object-oriented wrapper.
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class PcapFileTransportHandler extends AbstractPushTransportHandler {

	/**
	 * The logger instance for the pcap file wrapper.
	 */
	public static final Logger log = LoggerFactory.getLogger("pcap file");

	/**
	 * The name of the transport handler for query languages.
	 */
	private static final String name = "PcapFile";

	/**
	 * The options name for the file.
	 */
	private static final String fileKey = "file";

	/**
	 * The name of the file.
	 */
	private String filename;

	/**
	 * The Pcap object.
	 */
	private Pcap pcapObj;

	/**
	 * Empty default constructor. Needed to run as an OSGi service.
	 */
	public PcapFileTransportHandler() {
	}

	/**
	 * Creates a new transport handler.
	 *
	 * @param protocolHandler
	 *            The protocol handler to use.
	 * @param options
	 *            The options map.
	 */
	public PcapFileTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}

	/**
	 * Initializes the transport handler.
	 *
	 * @param options
	 *            The options map.
	 */
	private void init(OptionMap options) {
		options.checkRequiredException(fileKey);
		filename = options.get(fileKey);
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new PcapFileTransportHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler obj) {
		if (obj == null || !(obj instanceof PcapFileTransportHandler)) {
			return false;
		}

		PcapFileTransportHandler other = (PcapFileTransportHandler) obj;
		return (this.filename == null && other.filename == null)
				|| (this.filename != null && this.filename.equals(other.filename));
	}

	/**
	 * Closes the pcap object.
	 */
	private void closePCap() {
		if (pcapObj != null) {
			try {
				pcapObj.close();
			} catch (PcapClosedException e) {
				log.debug("error while closing pcap", e);
			}
		}
	}

	////////////////////
	// Read Pcap file //
	////////////////////

	@Override
	public void processInOpen() throws IOException {
		final StringBuilder errBuilder = new StringBuilder();
		pcapObj = Pcap.openOffline(filename, errBuilder);
		if (pcapObj == null) {
			throw new IOException("Could not open Pcap file!\n" + errBuilder.toString());
		}

		// Start new thread to finish processInOpen
		new Thread("Pcap Reader") {

			@Override
			public void run() {
				pcapObj.loop(Pcap.LOOP_INFINITE, new JPacketHandler<PcapFileTransportHandler>() {

					// The object holding all tcp information (including
					// payload)
					final Tcp tcp = new Tcp();

					@Override
					public void nextPacket(JPacket packet, PcapFileTransportHandler pcapHandler) {
						if (packet.hasHeader(tcp)) {
							byte[] bytes = tcp.getPayload();
							pcapHandler.fireProcess(ByteBuffer.wrap(bytes));
						}
					}

				}, PcapFileTransportHandler.this);

				fireDone();
			};

		}.start();

	}

	@Override
	public void processInClose() throws IOException {
		closePCap();
	}

	/////////////////////
	// Write Pcap file //
	/////////////////////

	@Override
	public void processOutOpen() throws IOException {
		throw new RuntimeException("PcapFileHandler is currently only implemented to READ files!");
	}

	@Override
	public void processOutClose() throws IOException {
		closePCap();
	}

	@Override
	public void send(byte[] message) throws IOException {
		throw new RuntimeException("PcapFileHandler is currently only implemented to READ files!");

	}
}